package com.study.springboot.controller;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.study.springboot.dao.noticeDAO;
import com.study.springboot.dto.noticeDTO;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class noticeController {

    @Autowired
    noticeDAO dao;

    //공지 작성 폼
    @RequestMapping("/admin/noticeWriteForm")
    public String noticeWriteForm() {
        return "admin/noticeWriteForm";
    }
       
    //공지 작성 처리
    @RequestMapping("/admin/noticeWrite")
    public String noticeWrite(
            @RequestParam("no_title") String no_title,
            @RequestParam("no_content") String no_content,
            @RequestParam("file") MultipartFile file
    		) throws Exception {

    	noticeDTO dto = new noticeDTO();
        dto.setNo_title(no_title);
        dto.setNo_content(no_content);
        
        // 외부 업로드 경로
        String uploadPath = "C:/upload/notice/";

        if (!file.isEmpty()) {
            String originalName = file.getOriginalFilename();

            // ⭐ UUID 파일명 생성
            String saveName = UUID.randomUUID().toString() + "_" + originalName;

            File saveFile = new File(uploadPath + saveName);
            file.transferTo(saveFile);

            // 👉 DB에는 저장된 파일명만
            dto.setNo_upload(saveName);
        } else {
            dto.setNo_upload("");
        }

        dao.no_writeDao(dto);
        return "redirect:/member/noticeList";
    }


      // 공지 목록
    @RequestMapping("/member/noticeList")
    public String noticeList(Model model) {
        model.addAttribute("no_list", dao.no_listDao());
        return "member/noticeList";
    }

 
     //공지 상세보기
    @RequestMapping("/member/noticeDetail")
    public String noticeDetail(
            @RequestParam("no_no") String no_no,
            Model model
    ) {
        model.addAttribute("notice", dao.no_viewDao(no_no));
        return "member/noticeDetail";
    }

  
    //공지 삭제
    @RequestMapping("/admin/noticeDelete")
    public String noticeDelete(
            @RequestParam("no_no") String no_no
    ) {
        dao.no_deleteDao(no_no);
        return "redirect:/member/noticeList";
    }
    
    //공지 수정
    @RequestMapping("/admin/noticeUpdateForm")
    public String noticeUpdateForm(
            @RequestParam("no_no") String no_no,
            Model model
    ) {
        model.addAttribute("notice", dao.no_viewDao(no_no));
        return "admin/noticeUpdateForm";
    }

    
    
    @RequestMapping("/admin/noticeUpdate")
    public String noticeUpdate(
            noticeDTO dto,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        String uploadPath = "C:/upload/notice/";

        if (!file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String saveName = UUID.randomUUID().toString() + "_" + originalName;

            File saveFile = new File(uploadPath + saveName);
            file.transferTo(saveFile);

            dto.setNo_upload(saveName);
        }

        dao.no_updateDao(dto);

        return "redirect:/member/noticeDetail?no_no=" + dto.getNo_no();
    }

}
