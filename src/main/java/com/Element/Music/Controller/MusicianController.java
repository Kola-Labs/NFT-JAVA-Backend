package com.Element.Music.Controller;

import com.Element.Music.Emun.Sex;
import com.Element.Music.Model.DAO.UserDAO.Musician;
import com.Element.Music.Service.MusicianService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class MusicianController {

    private final MusicianService musicianService;

    public MusicianController(MusicianService musicianService) {
        this.musicianService = musicianService;
    }

   /* @Configuration
    public class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/img/singerPic/**").addResourceLocations("file:/Users/jiangjiayi/Documents/github-workspace/music-website/music-server/img/singerPic/");
        }
    }*/

    //    添加歌手
    @ResponseBody
    @RequestMapping(value = "/singer/add", method = RequestMethod.POST)
    public Object addSinger(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        String name = req.getParameter("name").trim();
        String sex = req.getParameter("sex").trim();
        String pic = req.getParameter("pic").trim();
        String birth = req.getParameter("birth").trim();
        String location = req.getParameter("location").trim();
        String description = req.getParameter("description").trim();

        Musician musician = Musician.builder().representImagePath(pic).description(description).build();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        musician.setName(name);
        musician.setBirth(myBirth);
        musician.setLocation(location);
        musician.setSex(sex == "male" ? Sex.MALE : Sex.FEMALE);

        Musician res = musicianService.addMusician(musician);

        if (res != null) {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "添加成功");
            return jsonObject;
        } else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "添加失败");
            return jsonObject;
        }
    }

   /* //    返回所有歌手
    @RequestMapping(value = "/singer", method = RequestMethod.GET)
    public Object allSinger() {
        return singerService.allSinger();
    }

    //    根据歌手名查找歌手
    @RequestMapping(value = "/singer/name/detail", method = RequestMethod.GET)
    public Object singerOfName(HttpServletRequest req) {
        String name = req.getParameter("name").trim();
        return singerService.singerOfName(name);
    }

    //    根据歌手性别查找歌手
    @RequestMapping(value = "/singer/sex/detail", method = RequestMethod.GET)
    public Object singerOfSex(HttpServletRequest req) {
        String sex = req.getParameter("sex").trim();
        return singerService.singerOfSex(Integer.parseInt(sex));
    }

    //    删除歌手
    @RequestMapping(value = "/singer/delete", method = RequestMethod.GET)
    public Object deleteSinger(HttpServletRequest req) {
        String id = req.getParameter("id");
        return singerService.deleteSinger(Integer.parseInt(id));
    }*/

    //    更新歌手信息
    @ResponseBody
    @RequestMapping(value = "/singer/update", method = RequestMethod.POST)
    public Object updateSingerMsg(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        String id = req.getParameter("id").trim();
        String name = req.getParameter("name").trim();
        String sex = req.getParameter("sex").trim();
        String pic = req.getParameter("pic").trim();
        String birth = req.getParameter("birth").trim();
        String location = req.getParameter("location").trim();
        String description = req.getParameter("introduction").trim();

        Musician musician = Musician.builder().representImagePath(pic).description(description).build();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        musician.setName(name);
        musician.setBirth(myBirth);
        musician.setLocation(location);

        Musician res = musicianService.updateMusicianMsg(musician);
        if (res != null) {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "修改成功");
            return jsonObject;
        } else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "修改失败");
            return jsonObject;
        }
    }

    //    更新歌手头像
    @ResponseBody
    @RequestMapping(value = "/singer/avatar/update", method = RequestMethod.POST)
    public Object updateSingerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();

        if (avatorFile.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "文件上传失败！");
            return jsonObject;
        }
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "singerPic";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeAvatorPath = "/img/singerPic/" + fileName;
        try {
            avatorFile.transferTo(dest);
            Musician musician = Musician.builder().representImagePath(storeAvatorPath).build();
            musician.setId(id);
            boolean res = musicianService.updateSingerPic(musician);
            if (res) {
                jsonObject.put("code", 1);
                jsonObject.put("pic", storeAvatorPath);
                jsonObject.put("msg", "上传成功");
                return jsonObject;
            } else {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "上传失败");
                return jsonObject;
            }
        } catch (IOException e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "上传失败" + e.getMessage());
            return jsonObject;
        } finally {
            return jsonObject;
        }
    }
}