
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {
    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    SongRepository songRepository;

    @RequestMapping("/")
    public String index(Model model) {
        //First let's create an album
        Album album = new Album();

        album.setName("Mos def and Talib Kweli are Blackstar");
        album.setGenre("Rap");

        //Now let's create a song
        Song song = new Song();
        song.setTitle("Hiding like Thieves");
        song.setYear(1998);
        song.setDescription("It's about society...");

        //Add song to an empty list
        Set<Song> songs = new HashSet<Song>();
        songs.add(song);

        song = new Song();
        song.setTitle("Brown skin lady");
        song.setYear(1998);
        song.setDescription("It's about black women..");

        //Add the list of songs to the albums list
        album.setSongs(songs);

        //Save the Album to a database
        albumRepository.save(album);

        //Grab all the directors from the database and send them to the template
        model.addAttribute("albums", albumRepository.findAll());
        return "index";


    }

    @GetMapping("/add")
    public String courseForm(Model model) {
        model.addAttribute("song", new Song());
        model.addAttribute("albums", albumRepository.findAll());
        return "songForm";
    }

    @PostMapping("/process")
    public String processForm(@Valid Song song, BindingResult result, Model model) {
        if (result.hasErrors()) {

            return "songForm";
        }
        songRepository.save(song);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showSong(@PathVariable("id") long id, Model model) {
        model.addAttribute("song", songRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateSong(@PathVariable("id") long id, Model model) {
        model.addAttribute("song", songRepository.findById(id));
        return "songForm";
    }

    @RequestMapping("/delete/{id}")
    public String delSong(@PathVariable("id") long id) {
        songRepository.deleteById(id);
        return "redirect:/";
    }

    //========================= album======================
    @GetMapping("/addAlbum")
    public String albumForm(Model model) {
        model.addAttribute("album", new Album());
        return "albumForm";
    }

    @PostMapping("/processAlbum")
    public String processAlbum(@Valid Album album, BindingResult result) {
        if (result.hasErrors()) {
            return "albumForm";
        }
        albumRepository.save(album);
        return "redirect:/";
    }

    @RequestMapping("/list")
    public String listSongs(Model model) {
        model.addAttribute("songs", songRepository.findAll());
        model.addAttribute("albums", albumRepository.findAll());
        return "list";

    }

}



