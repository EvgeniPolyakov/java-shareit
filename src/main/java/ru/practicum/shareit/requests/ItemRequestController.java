package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemRequestDto> getAll() {
        log.info("Получен запрос GET /requests");
        return itemRequestService.getAll();
    }

    @PostMapping
    public ItemRequestDto add(@Valid @RequestBody ItemRequestDto request) {
        ItemRequestDto addedRequest = itemRequestService.add(request);
        log.info("Получен запрос POST (createRequest). Добавлен запрос на вещь: {}", request);
        return addedRequest;
    }

    @PatchMapping("/{id}")
    public ItemRequestDto update(@PathVariable("id") Long id, @Valid @RequestBody ItemRequestDto request) {
        ItemRequestDto updatedRequest = itemRequestService.update(id, request);
        log.info("Получен запрос PUT (updateRequest). Добавлен запрос на вещь: {}", request);
        return updatedRequest;
    }

    @GetMapping("/{id}")
    public ItemRequestDto getById(@PathVariable("id") Long id) {
        log.info("Получен запрос GET /requests по id {}", id);
        return itemRequestService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Получен запрос DELETE /requests по id {}", id);
        itemRequestService.delete(id);
    }
}
