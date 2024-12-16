package itmo.is.controller;

import itmo.is.dto.domain.PersonDto;
import itmo.is.dto.domain.request.CreatePersonRequest;
import itmo.is.dto.domain.request.UpdatePersonRequest;
import itmo.is.dto.domain.response.CountResponse;
import itmo.is.dto.domain.response.PercentageResponse;
import itmo.is.dto.history.PersonImportDto;
import itmo.is.model.domain.Color;
import itmo.is.model.domain.Country;
import itmo.is.model.security.Role;
import itmo.is.model.security.User;
import itmo.is.service.domain.PersonService;
import itmo.is.service.history.PersonHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
public class PersonRestController {
    private final PersonService personService;
    private final PersonHistoryService personHistoryService;

    @GetMapping
    public ResponseEntity<Page<PersonDto>> getAllPeople(
            @RequestParam(value = "name", required = false) String name,
            @PageableDefault Pageable pageable
    ) {
        Page<PersonDto> people = personService.findAllPeople(name, pageable);
        return ResponseEntity.ok(people);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable int id) {
        PersonDto person = personService.findPersonById(id);
        return ResponseEntity.ok(person);
    }

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody CreatePersonRequest request) {
        PersonDto createdPerson = personService.createPerson(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
    }

    @PostMapping(value = "/imports", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PersonImportDto> importFile(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.importFile(file));
    }

    @GetMapping(value = "/imports")
    public ResponseEntity<Page<PersonImportDto>> findAllImports(
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable
    ) {
        Page<PersonImportDto> result;
        if (user.getRole() == Role.ROLE_ADMIN) {
            result = personHistoryService.findAll(pageable);
        } else {
            result = personHistoryService.findAllByUser(user, pageable);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/imports/{id}/file")
    public ResponseEntity<ByteArrayResource> getFileByImportId(@PathVariable Long id) {
        ByteArrayResource file = personService.getImportFileByImportId(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("person-import-" + id + ".json")
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(file);
    }

    @PreAuthorize("@personSecurityService.hasEditRights(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> updatePerson(
            @PathVariable int id,
            @RequestBody UpdatePersonRequest request
    ) {
        PersonDto updatedPerson = personService.updatePerson(id, request);
        return ResponseEntity.ok(updatedPerson);
    }

    @PreAuthorize("@personSecurityService.isOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable int id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@personSecurityService.isOwner(#id)")
    @PutMapping("/{id}/allow-admin-editing")
    public ResponseEntity<Void> allowAdminEditing(@PathVariable int id) {
        personService.allowAdminEditing(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@personSecurityService.isOwner(#id)")
    @PutMapping("/{id}/deny-admin-editing")
    public ResponseEntity<Void> denyAdminEditing(@PathVariable int id) {
        personService.denyAdminEditing(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count-by-weight")
    public ResponseEntity<CountResponse> countPeopleByWeight(@RequestParam Integer weight) {
        CountResponse response = personService.countPeopleByExactWeight(weight);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count-by-weight-less-than")
    public ResponseEntity<CountResponse> countPeopleByWeightLessThan(@RequestParam Integer weight) {
        CountResponse response = personService.countPeopleByWeightLessThan(weight);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nationalities")
    public ResponseEntity<List<Country>> getDistinctNationalities() {
        List<Country> nationalities = personService.findDistinctNationalities();
        return ResponseEntity.ok(nationalities);
    }

    @GetMapping("/count-by-hair-color")
    public ResponseEntity<CountResponse> countPeopleByHairColor(@RequestParam Color color) {
        CountResponse response = personService.countPeopleByHairColor(color);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/percentage-by-eye-color")
    public ResponseEntity<PercentageResponse> calculatePercentageByEyeColor(@RequestParam Color color) {
        PercentageResponse response = personService.calculatePercentageOfPeopleByEyeColor(color);
        return ResponseEntity.ok(response);
    }
}
