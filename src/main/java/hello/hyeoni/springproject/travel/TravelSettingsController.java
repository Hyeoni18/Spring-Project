package hello.hyeoni.springproject.travel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.CurrentUser;
import hello.hyeoni.springproject.tag.Tag;
import hello.hyeoni.springproject.tag.TagForm;
import hello.hyeoni.springproject.tag.TagRepository;
import hello.hyeoni.springproject.tag.TagService;
import hello.hyeoni.springproject.travel.form.TravelDescriptionForm;
import hello.hyeoni.springproject.zone.Zone;
import hello.hyeoni.springproject.zone.ZoneForm;
import hello.hyeoni.springproject.zone.ZoneRepository;
import hello.hyeoni.springproject.zone.ZoneService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/travel/{path}/settings")
public class TravelSettingsController {

    private final TravelService travelService;
    private final TagService tagService;
    private final TagRepository tagRepository;
    private final ZoneService zoneService;
    private final ZoneRepository zoneRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;


    @GetMapping("/description")
    public String viewTravelSetting(@CurrentUser Account account, @PathVariable String path, Model model) {
        Travel travel = travelService.getTravelToUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(travel);
        model.addAttribute(modelMapper.map(travel, TravelDescriptionForm.class));
        return "travel/settings/description";
    }

    @PostMapping("/description")
    public String updateTravelInfo(@CurrentUser Account account, @PathVariable String path, @Valid TravelDescriptionForm travelDescriptionForm, Errors errors, RedirectAttributes redirectAttributes, Model model) {
        Travel travel = travelService.getTravelToUpdate(account, path);

        if (errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(travel);
            return "travel/settings/description";
        }

        travelService.updateTravelDescription(travel, travelDescriptionForm);
        redirectAttributes.addFlashAttribute("message", "모집 소개를 수정했습니다.");
        return "redirect:/travel/"+getPath(path)+"/settings/description";
    }

    private String getPath(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

    @GetMapping("/tags")
    public String travelTagsForm(@CurrentUser Account account, @PathVariable String path, Model model) throws JsonProcessingException {
        Travel travel = travelService.getTravelToUpdateTags(account, path);
        model.addAttribute(account);
        model.addAttribute(travel);

        model.addAttribute("tags", travel.getTags().stream().map(Tag::getTitle).collect(Collectors.toList()));
        List<String> allTagTitles = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTagTitles));

        return "travel/settings/tags";
    }

    @PostMapping("/tags/add")
    @ResponseBody
    public ResponseEntity addTag(@CurrentUser Account account, @PathVariable String path, @RequestBody TagForm tagForm) {
        Travel travel = travelService.getTravelToUpdateTags(account, path);
        Tag tag = tagService.findOrCreateNew(tagForm.getTagTitle());
        travelService.addTag(travel, tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tags/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentUser Account account, @PathVariable String path, @RequestBody TagForm tagForm) {
        Travel travel = travelService.getTravelToUpdateTags(account, path);
        Tag tag = tagService.findOrCreateNew(tagForm.getTagTitle());
        if(tag == null) {
            return ResponseEntity.badRequest().build();
        }
        travelService.removeTag(travel, tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/zones")
    public String travelZonesForm(@CurrentUser Account account, @PathVariable String path, Model model) throws JsonProcessingException {
        Travel travel = travelService.getTravelToUpdateZones(account, path);
        model.addAttribute(account);
        model.addAttribute(travel);

        model.addAttribute("zones",travel.getZones().stream().map(Zone::toString).collect(Collectors.toList()));
        List<String> allZoneNames = zoneRepository.findAll().stream().map(Zone::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZoneNames));

        return "travel/settings/zones";
    }

    @PostMapping("/zones/add")
    @ResponseBody
    public ResponseEntity addZone(@CurrentUser Account account, @PathVariable String path, @RequestBody ZoneForm zoneForm) {
        Travel travel = travelService.getTravelToUpdateZones(account, path);
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        travelService.addZone(travel, zone);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/zones/remove")
    @ResponseBody
    public ResponseEntity removeZone(@CurrentUser Account account, @PathVariable String path, @RequestBody ZoneForm zoneForm) {
        Travel travel = travelService.getTravelToUpdateZones(account, path);
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        travelService.removeZone(travel, zone);
        return ResponseEntity.ok().build();
    }

}
