package com.study.springboot.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.study.springboot.dao.member2DAO;
import com.study.springboot.dto.member2DTO;
import com.study.springboot.security.CustomUserDetails;
import com.study.springboot.service.MailService;
import com.study.springboot.service.member2Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class memberController {

    @Autowired
    member2DAO dao;
    @Autowired
	MailService service;
	@Autowired
	member2Service mService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* =========================
       공통: 로그인 사용자(member2DTO) 가져오기
       - 일반 로그인: CustomUserDetails
       - 소셜 로그인: OAuth2User (m_no / provider+providerId 기반)
    ========================= */
    private member2DTO getLoginMember(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) return null;

        Object principal = authentication.getPrincipal();

        // 1) 일반 로그인
        if (principal instanceof CustomUserDetails cud) {
            return cud.getMember();
        }

        // 2) 소셜 로그인
        if (principal instanceof OAuth2User oauthUser) {
            Map<String, Object> attr = oauthUser.getAttributes();

            // (권장) CustomOAuth2UserService에서 attributes에 m_no를 심어둔 경우
            Object mnoObj = attr.get("m_no");
            if (mnoObj != null) {
                String m_no = String.valueOf(mnoObj);
                return dao.m_viewDao(m_no);
            }

            // (차선) provider/providerId로 찾기
            String provider = (String) attr.get("provider");
            String providerId = (String) attr.get("providerId");
            if (provider != null && providerId != null) {
                return dao.findByProvider(provider, providerId);
            }
        }

        return null;
    }

    /* =========================
       공통: 비밀번호 존재 여부
       - 소셜 계정은 m_pw가 null일 수 있음
    ========================= */
    private boolean hasPassword(member2DTO loginUser) {
        return loginUser != null && loginUser.getM_pw() != null && !loginUser.getM_pw().isBlank();
    }

    /* =========================
       회원가입 폼
    ========================= */
    @RequestMapping("/memberWriteForm")
    public String memberWriteForm() {
        return "memberWriteForm";
    }

    /* =========================
       회원가입 (일반)
    ========================= */
    @RequestMapping("/memberWrite")
    public String memberWrite(member2DTO dto, RedirectAttributes ra) throws Exception {

        // 0) 이메일 trim
        dto.setM_email(dto.getM_email() != null ? dto.getM_email().trim() : null);

        // 1) 이메일 인증 체크
        if (!service.isEmailVerified(dto.getM_email())) {
            ra.addFlashAttribute("error", "이메일 인증을 완료해주세요.");
            return "redirect:/memberWriteForm";
        }

        // 2) 이메일 중복 체크(추가)
        if (dao.findByUserId(dto.getM_email()) != null) {
            ra.addFlashAttribute("error", "이미 가입된 이메일입니다.");
            return "redirect:/memberWriteForm";
        }

        // 3) 닉네임 중복 체크(이미 있음)
        if (mService.countByNickname(dto.getM_nickname()) > 0) {
            ra.addFlashAttribute("error", "이미 사용중인 닉네임입니다.");
            return "redirect:/memberWriteForm";
        }

        // 4) 비밀번호 암호화
        dto.setM_pw(passwordEncoder.encode(dto.getM_pw()));

        // 5) local 강제 세팅 (⭐ 무조건)
        dto.setM_provider("local");
        dto.setM_provider_id(null);

        mService.write(dto);

        return "redirect:/loginForm?signup=success";
    }

    
  //닉네임 중복 검사
  	@GetMapping("/member/checkNickname")
  	@ResponseBody
  	public boolean checkNickname(@RequestParam("m_nickname") String m_nickname) {

  		return mService.isNicknameAvailable(m_nickname);

  	}
  	
  	//이메일 중복 검사
  	@GetMapping("/member/checkEmail")
  	@ResponseBody
  	public boolean checkEmail(@RequestParam("m_email") String m_email) {
  	    member2DTO m = dao.findByUserId(m_email.trim());
  	    return (m == null); // true = 사용 가능
  	}


    /* =========================
       회원 목록 (ADMIN)
    ========================= */
    @RequestMapping("/admin/memberList")
    public String memberList(Authentication authentication, Model model) {

        member2DTO loginUser = getLoginMember(authentication);
        if (loginUser == null) return "redirect:/loginForm";

        if (!"ROLE_ADMIN".equals(loginUser.getM_auth())) {
            return "redirect:/accessDenied";
        }

        model.addAttribute("m_list", dao.m_listDao());
        return "admin/memberList";
    }

    /* =========================
       회원 정보
    ========================= */
    @RequestMapping("/member/memberDetail")
    public String memberDetail(Authentication authentication, Model model) {

        member2DTO loginUser = getLoginMember(authentication);
        if (loginUser == null) return "redirect:/loginForm";

        member2DTO dto = dao.m_viewDao(loginUser.getM_no());

        model.addAttribute("memberDetail", dto);
        model.addAttribute("loginUser", loginUser);

        return "member/memberDetail";
    }

    /* =========================
       회원 정보 수정 폼
       - 비밀번호 확인(ROLE_PASSWORD_CONFIRMED) 필요
       - 소셜 계정이 비밀번호 없으면 "비밀번호 설정"으로 유도
    ========================= */
    @RequestMapping("/member/memberUpdateForm")
    public String memberUpdateForm(Authentication authentication, Model model) {

        member2DTO loginUser = getLoginMember(authentication);
        if (loginUser == null) return "redirect:/loginForm";

        // 소셜 계정 & 비밀번호 없음 → 비밀번호 설정 유도
        if (!hasPassword(loginUser)) {
            return "redirect:/member/setPasswordForm?next=/member/memberUpdateForm";
        }

        // 비밀번호 확인 권한 체크
        if (authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_PASSWORD_CONFIRMED"))) {
            return "redirect:/member/passwordCheckForm?mode=update";
        }

        member2DTO dto = dao.m_viewDao(loginUser.getM_no());
        model.addAttribute("memberUpdate", dto);
        return "member/memberUpdateForm";
    }


    /* =========================
       회원 정보 수정
       - 비밀번호 없음(소셜) 상태면 막고 비밀번호 설정 유도
    ========================= */
    @RequestMapping("/member/memberUpdate")
    public String memberUpdate(member2DTO dto,
                               Authentication authentication,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        member2DTO loginUser = getLoginMember(authentication);
        if (loginUser == null) return "redirect:/loginForm";

        // 소셜 계정 & 비밀번호 없음 → 비밀번호 설정 유도
        if (!hasPassword(loginUser)) {
            return "redirect:/member/setPasswordForm?next=/member/memberUpdateForm";
        }

        // 비밀번호 확인 권한 체크
        if (authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_PASSWORD_CONFIRMED"))) {
            return "redirect:/member/passwordCheckForm?mode=update";
        }

        member2DTO targetUser = dao.m_viewDao(dto.getM_no());
        if (targetUser == null) return "redirect:/accessDenied";

        // 권한 체크: 본인 또는 관리자
        if (!loginUser.getM_no().equals(targetUser.getM_no())
                && !"ROLE_ADMIN".equals(loginUser.getM_auth())) {
            return "redirect:/accessDenied";
        }

        // 빈값이면 기존값 유지
        if (dto.getM_name() == null || dto.getM_name().isBlank()) dto.setM_name(targetUser.getM_name());
        if (dto.getM_nickname() == null || dto.getM_nickname().isBlank()) dto.setM_nickname(targetUser.getM_nickname());
        if (dto.getM_tel() == null || dto.getM_tel().isBlank()) dto.setM_tel(targetUser.getM_tel());
        if (dto.getM_addr() == null || dto.getM_addr().isBlank()) dto.setM_addr(targetUser.getM_addr());

        // 비밀번호 처리
        if (dto.getM_pw() != null && !dto.getM_pw().isBlank()) {
            dto.setM_pw(passwordEncoder.encode(dto.getM_pw()));
        } else {
            dto.setM_pw(targetUser.getM_pw());
        }

        dao.m_updateDao(dto);

        // 수정 후 로그아웃
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:/loginForm";
    }

    
    

    /* =========================
       회원 탈퇴 (본인/관리자)
       - 비밀번호 확인(ROLE_PASSWORD_CONFIRMED) 필요
       - 소셜 계정 비밀번호 없으면 설정 후 진행
    ========================= */
    @PostMapping("/member/memberDelete")
    public String memberDelete(@RequestParam("m_no") String m_no,
                               Authentication authentication,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        member2DTO loginUser = getLoginMember(authentication);
        if (loginUser == null) return "redirect:/";

        if (!hasPassword(loginUser)) {
            return "redirect:/member/setPasswordForm?next=/member/memberDeleteConfirm";
        }

        if (authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_PASSWORD_CONFIRMED"))) {
            return "redirect:/member/passwordCheckForm?mode=delete";
        }

        member2DTO targetUser = dao.m_viewDao(m_no);
        if (targetUser == null) return "redirect:/accessDenied";

        if (!loginUser.getM_no().equals(targetUser.getM_no())
                && !"ROLE_ADMIN".equals(loginUser.getM_auth())) {
            return "redirect:/accessDenied";
        }

        // ✅ DB + ES 같이 삭제
        mService.delete(m_no);

        if (loginUser.getM_no().equals(targetUser.getM_no())) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            return "redirect:/";
        }

        return "redirect:/admin/memberList";
    }

    /* =========================
       강퇴 (ADMIN)
    ========================= */
    @RequestMapping("/admin/member/memberDelete")
    public String adminMemberDelete(@RequestParam("m_no") String m_no,
                                    Authentication authentication) throws Exception {

        member2DTO loginUser = getLoginMember(authentication);
        if (loginUser == null) return "redirect:/loginForm";

        if (!"ROLE_ADMIN".equals(loginUser.getM_auth())) {
            return "redirect:/accessDenied";
        }

        // ✅ DB + ES 같이 삭제
        mService.delete(m_no);

        return "redirect:/admin/memberList";
    }


    /* =========================
       주소 팝업
    ========================= */
    @RequestMapping("/jusoPopup")
    public String jusoPopup() {
        return "jusoPopup";
    }

    /* =========================
       비밀번호 확인 폼 (수정/삭제 전에)
       - 소셜 계정이 비밀번호 없으면 setPasswordForm으로 보냄
       - mode: update / delete
    ========================= */
    @GetMapping("/member/passwordCheckForm")
    public String passwordCheckForm(@RequestParam("mode") String mode,
                                    Authentication authentication,
                                    Model model) {

        member2DTO loginUser = getLoginMember(authentication);
        if (loginUser == null) return "redirect:/loginForm";

        // 소셜 계정 & 비밀번호 없음 → 비밀번호 설정 먼저
        if (!hasPassword(loginUser)) {
            String next = "update".equals(mode) ? "/member/memberUpdateForm" : "/member/memberDeleteConfirm";
            return "redirect:/member/setPasswordForm?next=" + next;
        }

        model.addAttribute("mode", mode);
        model.addAttribute("m_no", loginUser.getM_no());
        return "member/passwordCheckForm";
    }

    /* =========================
       비밀번호 확인 처리
    ========================= */
    @PostMapping("/member/passwordCheck")
    public String passwordCheck(@RequestParam("m_password") String m_password,
                                @RequestParam("mode") String mode,
                                Authentication authentication,
                                Model model) {

        member2DTO loginUser = getLoginMember(authentication);
        if (loginUser == null) return "redirect:/loginForm";

        // 소셜 계정 & 비밀번호 없음 → 비밀번호 설정 먼저
        if (!hasPassword(loginUser)) {
            String next = "update".equals(mode) ? "/member/memberUpdateForm" : "/member/memberDeleteConfirm";
            return "redirect:/member/setPasswordForm?next=" + next;
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(m_password, loginUser.getM_pw())) {
            model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("mode", mode);
            return "member/passwordCheckForm";
        }

        // 권한 추가
        Collection<GrantedAuthority> newAuth = new ArrayList<>(authentication.getAuthorities());
        newAuth.add(new SimpleGrantedAuthority("ROLE_PASSWORD_CONFIRMED"));

        Authentication newAuthentication =
                new UsernamePasswordAuthenticationToken(
                        authentication.getPrincipal(),
                        authentication.getCredentials(),
                        newAuth
                );

        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        return "update".equals(mode)
                ? "redirect:/member/memberUpdateForm"
                : "redirect:/member/memberDeleteConfirm";
    }

    /* =========================
       탈퇴 확인 화면
    ========================= */
    @GetMapping("/member/memberDeleteConfirm")
    public String memberDeleteConfirm(Authentication authentication, Model model) {

        member2DTO loginUser = getLoginMember(authentication);
        if (loginUser == null) return "redirect:/loginForm";

        // 소셜 계정 & 비밀번호 없음 → 비밀번호 설정 후 진행
        if (!hasPassword(loginUser)) {
            return "redirect:/member/setPasswordForm?next=/member/memberDeleteConfirm";
        }

        model.addAttribute("loginUser", loginUser);
        return "member/memberDeleteConfirm";
    }

    /* =========================
       [A] 소셜 계정 비밀번호 설정 폼
       - next: 설정 후 이동할 URL (기본: /member/memberDetail)
    ========================= */
    @GetMapping("/member/setPasswordForm")
    public String setPasswordForm(@RequestParam(value = "next", required = false) String next,
                                  Authentication authentication,
                                  Model model) {

        member2DTO loginUser = getLoginMember(authentication);
        if (loginUser == null) return "redirect:/loginForm";

        // 이미 비밀번호 있으면 굳이 설정할 필요 없음
        if (hasPassword(loginUser)) {
            return "redirect:" + (next != null ? next : "/member/memberDetail");
        }

        model.addAttribute("next", next != null ? next : "/member/memberDetail");
        model.addAttribute("m_no", loginUser.getM_no());
        return "member/setPasswordForm";
    }

    /* =========================
       [A] 소셜 계정 비밀번호 설정 처리
       - 비밀번호 저장 후 로그아웃(권장)
    ========================= */
    @PostMapping("/member/setPassword")
    public String setPassword(@RequestParam("newPassword") String newPassword,
                              @RequestParam("newPasswordConfirm") String newPasswordConfirm,
                              @RequestParam(value = "next", required = false) String next,
                              Authentication authentication,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) {

        member2DTO loginUser = getLoginMember(authentication);
        if (loginUser == null) return "redirect:/loginForm";

        if (newPassword == null || newPassword.isBlank()) {
            model.addAttribute("msg", "비밀번호를 입력해주세요.");
            model.addAttribute("next", next != null ? next : "/member/memberDetail");
            model.addAttribute("m_no", loginUser.getM_no());
            return "member/setPasswordForm";
        }

        if (!newPassword.equals(newPasswordConfirm)) {
            model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("next", next != null ? next : "/member/memberDetail");
            model.addAttribute("m_no", loginUser.getM_no());
            return "member/setPasswordForm";
        }

        // 현재 값 조회
        member2DTO current = dao.m_viewDao(loginUser.getM_no());

        // ✅ m_updateDao가 m_pw/m_name/m_nickname/m_tel/m_addr를 업데이트하므로
        //    null 덮어쓰기 방지를 위해 현재값 채워서 업데이트
        member2DTO dto = new member2DTO();
        dto.setM_no(current.getM_no());
        dto.setM_pw(passwordEncoder.encode(newPassword));
        dto.setM_name(current.getM_name());
        dto.setM_nickname(current.getM_nickname());
        dto.setM_tel(current.getM_tel());
        dto.setM_addr(current.getM_addr());

        dao.m_updateDao(dto);

        // 비밀번호 설정 후 로그아웃(다시 로그인 유도)
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:/loginForm";
    }
    
    @GetMapping("/debug/pwcheck")
    @ResponseBody
    public String pwcheck(@RequestParam("email") String email,
                          @RequestParam("pw") String pw) {
        member2DTO m = dao.findByUserId(email.trim());
        if (m == null) return "NO USER";
        return passwordEncoder.matches(pw, m.getM_pw()) ? "MATCH" : "NOT MATCH";
    }

    
    
}
