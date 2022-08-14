package pl.cyryl.finalproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pl.cyryl.finalproject.app.photo.ItemPhoto.ItemPhoto;
import pl.cyryl.finalproject.app.photo.ItemPhoto.ItemPhotoRepository;
import pl.cyryl.finalproject.app.photo.ProfilePicture.ProfilePicture;
import pl.cyryl.finalproject.app.photo.ProfilePicture.ProfilePictureRepository;
import pl.cyryl.finalproject.util.FilesUtil;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Controller
public class TestController {

    private final ItemPhotoRepository itemPhotoRepository;
    private final ProfilePictureRepository profilePictureRepository;
    private final FilesUtil filesUtil;

    public TestController(ItemPhotoRepository itemPhotoRepository, ProfilePictureRepository profilePictureRepository, FilesUtil filesUtil) {
        this.itemPhotoRepository = itemPhotoRepository;
        this.profilePictureRepository = profilePictureRepository;
        this.filesUtil = filesUtil;
    }

    @Value("${app.user.item-images.location}")
    private String loc;

    @GetMapping("/")
    @ResponseBody
    public String start(HttpSession session){
        //session.setAttribute("userId", 1L);
        return loc;
    }

    @GetMapping("/other")
    @ResponseBody
    public String otherUser(HttpSession session){
        session.setAttribute("userId", 2L);
        return loc;
    }

    @GetMapping("/admic")
    @ResponseBody
    public String anotherUser(HttpSession session){
        //session.setAttribute("userId", 4L);
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "";
    }

    @GetMapping("/admin/ad")
    public String start2(){
        return "/admin";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/add")
    public String addPhoto(){
        return "/add";
    }

    @PostMapping("/add")
    public String savePhoto(@RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        ItemPhoto photo = filesUtil.getPhotoWithPath(new ItemPhoto(), multipartFile);
        photo = itemPhotoRepository.save(photo);
        filesUtil.saveItemPhoto(photo, multipartFile);

        model.addAttribute("photo", photo);
        model.addAttribute("photoDir", filesUtil.getItemPhotosDirectory());
        return "/item/display";
    }

    @GetMapping("/addp")
    public String addProfile(){
        return "/add";
    }

    @PostMapping("/addp")
    public String saveProfile(@RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        ProfilePicture profilePicture = filesUtil.getPhotoWithPath(new ProfilePicture(), multipartFile);
        profilePicture.setPublicPicture(true);
        profilePicture = profilePictureRepository.save(profilePicture);
        filesUtil.saveProfilePicture(profilePicture , multipartFile);

        model.addAttribute("photo", profilePicture);
        model.addAttribute("photoDir", filesUtil.getProfilePicturesDirectory());
        return "/item/display";
    }

//    @GetMapping("/display")
//    public String showPhoto(){
//        return "/item/display";
//    }

}
