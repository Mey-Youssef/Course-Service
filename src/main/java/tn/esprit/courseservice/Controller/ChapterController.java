package tn.esprit.courseservice.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.courseservice.Entity.Chapter;
import tn.esprit.courseservice.Service.Client.ChapterService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Chapter")
@RequiredArgsConstructor
public class ChapterController {
    @Autowired
     ChapterService chapterService;
    @PostMapping("/add")
    public ResponseEntity<Object> addChapter(
            @RequestParam int courseId,
            @RequestParam int tutorId,
            @RequestBody Chapter chapter) {
        try {
            Chapter savedChapter = chapterService.addChapter(courseId, tutorId, chapter);
            return ResponseEntity.ok(savedChapter);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Collections.singletonMap("message", e.getReason()));
        }
    }
    @PatchMapping("/{chapterId}/update")
    public ResponseEntity<Object> updateChapter(
            @PathVariable int chapterId,
            @RequestParam int tutorId,
            @RequestBody Map<String, Object> updates) {
        try {
            Chapter updatedChapter = chapterService.updateChapterPartially(chapterId, tutorId, updates);
            return ResponseEntity.ok(updatedChapter);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Collections.singletonMap("message", e.getReason()));
        }
    }
    @DeleteMapping("/{chapterId}/delete")
    public ResponseEntity<Object> deleteChapter(
            @PathVariable int chapterId,
            @RequestParam int tutorId) {
        try {
            chapterService.deleteChapter(chapterId, tutorId);
            return ResponseEntity.ok(Collections.singletonMap("message", "Chapitre supprimé avec succès"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Collections.singletonMap("message", e.getReason()));
        }
    }
    @GetMapping("/{chapterId}")
    public ResponseEntity<Object> getChapterById(@PathVariable int chapterId) {
        try {
            Chapter chapter = chapterService.getChapterById(chapterId);
            return ResponseEntity.ok(chapter);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Collections.singletonMap("message", e.getReason()));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Object> getChaptersByCourse(
            @PathVariable int courseId,
            @RequestParam int tutorId) {
        try {
            List<Chapter> chapters = chapterService.getChaptersByCourse(courseId, tutorId);

            if (chapters.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "Aucun chapitre trouvé pour ce cours"));
            }

            return ResponseEntity.ok(chapters);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Collections.singletonMap("message", e.getReason()));
        }
    }




}
