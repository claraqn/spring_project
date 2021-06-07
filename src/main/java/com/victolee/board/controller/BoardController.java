package com.victolee.board.controller;

import com.victolee.board.dto.BoardDto;
import com.victolee.board.dto.FileDto;
import com.victolee.board.service.BoardService;
import com.victolee.board.service.FileService;
import com.victolee.board.util.MD5Generator;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@AllArgsConstructor
public class BoardController {
    private BoardService boardService;
    private FileService fileService;

    /* 게시글 목록 */
    @GetMapping("/")
    public String list(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum) {
        List<BoardDto> boardList = boardService.getBoardlist(pageNum);
        Integer[] pageList = boardService.getPageList(pageNum);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);

        return "board/list.html";
    }


    /* 게시글 상세 */
    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        BoardDto boardDTO = boardService.getPost(no);
        FileDto fileDTo = fileService.getFile(no);

        model.addAttribute("boardDto", boardDTO);
        model.addAttribute("fileDto", fileDTo);
        return "board/detail.html";
    }


    /* 게시글 쓰기 */
    @GetMapping("/post")
    public String write() {
        return "board/write.html";
    }

    @PostMapping("/post")
    public String write(@RequestParam("file") MultipartFile files, BoardDto boardDto) {
//        boardService.savePost(boardDto);
//        return "redirect:/";
        try {
            String origFilename = files.getOriginalFilename();
//            String filename = new MD5Generator(origFilename).toString();
            String filename = origFilename.toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
//            String savePath = System.getProperty("user.dir") + "\\files";
            String savePath = System.getProperty("user.dir") + "\\src\\main\\resources"+"\\static\\img";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + filename;
//            String filePath = savePath + filename;
            files.transferTo(new File(filePath));

            FileDto fileDto = new FileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);


            Long fileId = fileService.saveFile(fileDto);
            boardDto.setFileId(fileId);
            boardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }




    /* 게시글 수정 */
    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        BoardDto boardDTO = boardService.getPost(no);
        FileDto fileDTo = fileService.getFile(no);

        model.addAttribute("boardDto", boardDTO);
        model.addAttribute("fileDto", fileDTo);

        return "board/update.html";
    }

    @PutMapping("/post/edit/{no}")
    public String update(@RequestParam("file") MultipartFile files, BoardDto boardDTO) {
//        boardService.savePost(boardDTO);
//
//        return "redirect:/";
        try {
            String origFilename = files.getOriginalFilename();
//            String filename = new MD5Generator(origFilename).toString();
            String filename = origFilename.toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
//            String savePath = System.getProperty("user.dir") + "\\files";
            String savePath = System.getProperty("user.dir") + "\\src\\main\\resources"+"\\static\\img";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + filename;
//            String filePath = savePath + filename;
            files.transferTo(new File(filePath));

            FileDto fileDto = new FileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);


            Long fileId = fileService.saveFile(fileDto);
            boardDTO.setFileId(fileId);
            boardService.savePost(boardDTO);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    /* 게시글 삭제 */
    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") Long no) {
        boardService.deletePost(no);

        return "redirect:/";
    }

    @GetMapping("/board/search")
    public String search(@RequestParam(value="keyword") String keyword, Model model) {
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);

        model.addAttribute("boardList", boardDtoList);

        return "board/list.html";
    }

//    @GetMapping("/download/{fileId}")
//    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
//        FileDto fileDto = fileService.getFile(fileId);
//        Path path = Paths.get(fileDto.getFilePath());
//        Resource resource = new InputStreamResource(Files.newInputStream(path));
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
//                .body(resource);
//    }

}
