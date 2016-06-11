package com.will.imlearntest.controller;

import com.will.imlearntest.bo.ChatRecordBo;
import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.po.FriendshipPo;
import com.will.imlearntest.vo.BaseResultVo;
import com.will.imlearntest.vo.PersonalInfoVo;
import com.will.imlearntest.vo.UserStatusVo;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.registry.infomodel.User;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by willl on 5/18/16.
 */
@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserBo userBo;


    @RequestMapping("login")
    public @ResponseBody BaseResultVo login(@ModelAttribute("email") String email,
                       @ModelAttribute("password") String password,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        String oEmail = (String)request.getSession().getAttribute("fromEmail");
        request.getSession().setAttribute("fromEmail", email);
        request.getSession().setAttribute("toEmail", "");
        request.getSession().setAttribute("listStatus", "recent");
        request.getSession().setAttribute("boxStatus", "nothing");
        if (userBo.login(email, password)) {
            if (oEmail != null)
                userBo.userLogout(oEmail);
            request.getSession().removeAttribute("recentList");
            refreshRecent(request);
            Map<String, Map<String, UserStatusVo>> res = userBo.listFriends((String)request.getSession().getAttribute("fromEmail"));
            request.getSession().setAttribute("friendList", res);
            PersonalInfoVo p = userBo.getPersonalInfo(email);
            request.getSession().setAttribute("myPic", p.getPic());
            return BaseResultVo.success;
        } else
            return new BaseResultVo(0, "certification failed");
    }

    @RequestMapping("register")
    @ResponseBody
    public BaseResultVo register(@ModelAttribute("username") String username,
                                 @ModelAttribute("email") String email,
                                 @ModelAttribute("password") String password,
                                 HttpServletRequest request, HttpServletResponse response) {
        int res = userBo.register(username, email, password);
        if (res == 0)
            return new BaseResultVo(0, "The email has been register");
        return BaseResultVo.success;
    }

    @RequestMapping("friendList")
    public String friendList(HttpServletRequest request, HttpServletResponse response)
                        throws IOException{
        request.getSession().setAttribute("listStatus", "friendList");
        Map<String, Map<String, UserStatusVo>> res = userBo.listFriends((String)request.getSession().getAttribute("fromEmail"));
        request.getSession().setAttribute("friendList", res);
        /*for (UserStatusVo item : res) {
            System.out.println(item.getUsername() + " " + item.getStatus());
        }*/
        return "friendlist";
    }

    @RequestMapping("detailinfo")
    public String detailinfo(@ModelAttribute("email") String email,
                             HttpServletRequest request, HttpServletResponse response) {
        String fromEmail = (String)request.getSession().getAttribute("fromEmail");
        if (email.equals("self"))
            email = fromEmail;
        else {
            String nowGroup = userBo.getNowGroup(fromEmail, email);
            request.getSession().setAttribute("nowGroup", nowGroup);
        }
        request.getSession().setAttribute("boxStatus", "detail:" + email);
        request.getSession().setAttribute("detailEmail", email);
        PersonalInfoVo personalInfoVo = userBo.getPersonalInfo(email);
        request.getSession().setAttribute("personalInfoVo", personalInfoVo);
        return "detailinfo";
    }

    @RequestMapping("addGroup")
    @ResponseBody
    public BaseResultVo addGroup(@ModelAttribute("newGroup") String newGroup,
                         HttpServletRequest request, HttpServletResponse response) {
        String email = (String)request.getSession().getAttribute("fromEmail");
        int res = userBo.addGroup(email, newGroup);
        if (res > 0)
            return BaseResultVo.success;
        else
            return new BaseResultVo(0, "There maybe have the same group.");
    }

    @RequestMapping("recent")
    public String recent(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("listStatus", "recent");
        refreshRecent(request);
        return "recent";
    }

    private void refreshRecent(HttpServletRequest request) {

        Map<String, UserStatusVo> list = (Map<String, UserStatusVo>)request.getSession().getAttribute("recentList");
        if (list == null) {
            list = new HashMap<String, UserStatusVo>();
            list.clear();
            UserStatusVo tmp = new UserStatusVo();
            tmp.setHaveUnread(false);
            list.put("systemInfo@sys.com", tmp);
        }
        list = userBo.recentList((String)request.getSession().getAttribute("fromEmail"), list);
        request.getSession().setAttribute("recentList", list);
    }

    @RequestMapping("groupManager")
    public String groupManager(HttpServletRequest request, HttpServletResponse response) {
        return "groupManager";
    }

    @RequestMapping("removeGroup")
    @ResponseBody
    public BaseResultVo removeGroup(@ModelAttribute("groupName") String groupName,
                                    HttpServletRequest request, HttpServletResponse response) {
        String fromEmail = (String)request.getSession().getAttribute("fromEmail");
        Map<String, Map<String, UserStatusVo>> list = (Map<String, Map<String, UserStatusVo>>) request.getSession().getAttribute("friendList");
        if (!list.get(groupName).isEmpty())
            return new BaseResultVo(0, "Non-empty group can not be removed");
        userBo.removeGroup(fromEmail, groupName);
        return BaseResultVo.success;
    }

    @RequestMapping("updateGroupName")
    public void updateGroupName(@ModelAttribute("oriName") String oriName,
                                @ModelAttribute("newName") String newName,
                                HttpServletRequest request, HttpServletResponse response) {
        try {
            oriName = URLDecoder.decode(oriName, "UTF-8");
            newName = URLDecoder.decode(newName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fromEmail = (String) request.getSession().getAttribute("fromEmail");
        userBo.updateGroupName(fromEmail, oriName, newName);
    }

    @RequestMapping("uploadHead")
    @ResponseBody
    public BaseResultVo uploadHead(HttpServletRequest request, HttpServletResponse response) {
        String email = (String)request.getSession().getAttribute("fromEmail");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile mFile = multipartRequest.getFile("pic");
        try {
            if (mFile == null || mFile.isEmpty())
                return new BaseResultVo(1, "The file is empty");
            String fileName = mFile.getOriginalFilename();
//            System.out.println(fileName);
            if (fileName != null && !"".equals(fileName.trim())) {
                long size = mFile.getSize();
                if (size > (1024 * 1024))
                    return new BaseResultVo(2, "The file size is limited in 1MB");

                String extName = fileName.substring(fileName.indexOf("."));
                String root=request.getSession().getServletContext().getRealPath("/");
//                System.out.println(root);
                String path = "/userResources/" + email + "_head" + extName;
                File f = new File(root + path);
                if (!f.exists()) {
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                }
                mFile.transferTo(f);
                userBo.updatePic(email, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResultVo(0, "upload failed");
        }
        return BaseResultVo.success;
    }
}
