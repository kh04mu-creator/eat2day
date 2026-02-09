package com.study.springboot.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.springboot.dao.answerDAO;
import com.study.springboot.dao.questionDAO;
import com.study.springboot.dto.answerDTO;
import com.study.springboot.dto.questionDTO;
import com.study.springboot.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class qnaController {

    private final questionDAO questionDAO;
    private final answerDAO answerDAO;

    /* =========================
       회원: QnA 목록
       ========================= */
    @GetMapping("/member/qnaList")
    public String qnaList(
            @RequestParam(value="onlyMine", defaultValue="false") boolean onlyMine,
            @AuthenticationPrincipal CustomUserDetails user,
            Model model
    ) {
        List<questionDTO> list = questionDAO.q_listDao();

        String loginMno = (user != null ? user.getMno() : null);

        boolean isAdmin = false;
        if (user != null && user.getAuthorities() != null) {
            isAdmin = user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));
        }

        if (onlyMine && loginMno != null) {
            list = list.stream()
                    .filter(q -> loginMno.equals(q.getM_no()))
                    .toList();
        }

        // ✅ 질문별 답변 개수 map(q_no -> cnt)
        var counts = answerDAO.a_countByQno();
        java.util.Map<String, Integer> answerCountMap = new java.util.HashMap<>();
        for (var c : counts) answerCountMap.put(c.getQ_no(), c.getCnt());

        model.addAttribute("list", list);
        model.addAttribute("answerCountMap", answerCountMap);
        model.addAttribute("onlyMine", onlyMine);
        model.addAttribute("loginMno", loginMno);
        model.addAttribute("isAdmin", isAdmin);

        return "member/qnaList";
    }

    
    /* =========================
       토글 눌렀을 때 답변 불러오는 api
    ========================= */
    @GetMapping("/member/qnaAnswers")
    @ResponseBody
    public List<answerDTO> qnaAnswers(
            @RequestParam("q_no") String q_no,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        questionDTO q = questionDAO.q_viewDao(q_no);

        String loginMno = (user != null ? user.getMno() : null);
        boolean isOwner = (loginMno != null && q != null && loginMno.equals(q.getM_no()));

        boolean isAdmin = false;
        if (user != null && user.getAuthorities() != null) {
            isAdmin = user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));
        }

        boolean isSecret = (q != null && "Y".equals(q.getQ_secret()));
        boolean canRead = (!isSecret) || isOwner || isAdmin;

        // 비밀글인데 권한 없으면 답변도 숨김
        if (!canRead) return java.util.Collections.emptyList();

        return answerDAO.a_listByQno(q_no);
    }

    
    



    /* =========================
       회원: 질문 작성 폼
       ========================= */
    @GetMapping("/member/questionWriteForm")
    public String questionWriteForm(Model model) {
        model.addAttribute("dto", new questionDTO());
        return "member/questionWriteForm";
    }

    /* =========================
       회원: 질문 등록 처리
       ========================= */
    @PostMapping("/member/questionWrite")
    public String questionWrite(
            questionDTO dto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        // 로그인 mno 자동 넣기
        if (user != null) dto.setM_no(user.getMno());

        // 체크박스 미체크면 null로 올 수 있음 -> N 처리
        if (dto.getQ_secret() == null) dto.setQ_secret("N");

        questionDAO.q_writeDao(dto);
        return "redirect:/member/qnaList";
    }

    

    /* =========================
       관리자: 답변 작성 폼
       ========================= */
    @GetMapping("/admin/answerWriteForm")
    public String anserWriteForm(@RequestParam("q_no") String q_no, Model model) {
        model.addAttribute("q", questionDAO.q_viewDao(q_no));
        model.addAttribute("answers", answerDAO.a_listByQno(q_no));
        model.addAttribute("q_no", q_no);
        return "admin/answerWriteForm";
    }

    /* =========================
       관리자: 답변 등록 처리
       ========================= */
    @PostMapping("/admin/answerWrite")
    public String anserWrite(answerDTO dto) {
        answerDAO.a_writeDao(dto);
        return "redirect:/admin/answerWriteForm?q_no=" + dto.getQ_no();
    }
    
    
    /* =========================
      관리자: QnA 목록
    ========================= */
    @GetMapping("/admin/qnaManage")
    public String qnaManage(Model model) {
        List<questionDTO> list = questionDAO.q_listDao();

        var counts = answerDAO.a_countByQno();
        java.util.Map<String, Integer> answerCountMap = new java.util.HashMap<>();
        for (var c : counts) answerCountMap.put(c.getQ_no(), c.getCnt());

        // ✅ 질문별 최신 답변 map(q_no -> latestAnswer)
        var latestList = answerDAO.a_latestByQuestion();
        java.util.Map<String, com.study.springboot.dto.AnswerLatestDTO> latestAnswerMap = new java.util.HashMap<>();
        for (var a : latestList) latestAnswerMap.put(a.getQ_no(), a);

        model.addAttribute("list", list);
        model.addAttribute("answerCountMap", answerCountMap);
        model.addAttribute("latestAnswerMap", latestAnswerMap);

        return "admin/qnaManage";
    }

    
    @PostMapping("/admin/answerDelete")
    public String answerDelete(
            @RequestParam("a_no") String a_no,
            @RequestParam("q_no") String q_no,
            @RequestParam(value="redirect", required=false) String redirect
    ) {
        answerDAO.a_deleteDao(a_no);

        // qnaManage에서 눌렀으면 qnaManage로 복귀
        if ("qnaManage".equals(redirect)) {
            return "redirect:/admin/qnaManage";
        }

        // 기본은 기존대로 answerWriteForm로 복귀
        return "redirect:/admin/answerWriteForm?q_no=" + q_no;
    }
    
    @PostMapping("/admin/questionDelete")
    public String adminQuestionDelete(
            @RequestParam("q_no") String q_no,
            @RequestParam(value="redirect", required=false) String redirect
    ) {
        questionDAO.q_deleteDao(q_no); // FK ON DELETE CASCADE면 답변도 같이 삭제됨

        // qnaManage에서 눌렀으면 qnaManage로
        if ("qnaManage".equals(redirect) || redirect == null) {
            return "redirect:/admin/qnaManage";
        }

        // 혹시 다른 화면에서 쓰게 되면 대비
        return "redirect:" + redirect;
    }
    
    @PostMapping("/member/questionDelete")
    public String memberQuestionDelete(
        @RequestParam("q_no") String q_no,
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        questionDTO q = questionDAO.q_viewDao(q_no);
        if (q == null) return "redirect:/member/qnaList";

        String loginMno = (user != null ? user.getMno() : null);

        // 본인 글만 삭제
        if (loginMno == null || !loginMno.equals(q.getM_no())) {
            return "redirect:/member/qnaList";
        }

        questionDAO.q_deleteDao(q_no);
        return "redirect:/member/qnaList";
    }




}
