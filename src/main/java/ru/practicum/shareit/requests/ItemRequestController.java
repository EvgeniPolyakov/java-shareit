package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestMapper;
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
        log.info("Получен запрос по пути GET /requests");
        return ItemRequestMapper.toItemRequestDtoList(itemRequestService.getAll());
    }

    @PostMapping
    public ItemRequestDto add(@Valid @RequestBody ItemRequestDto requestDto) {
        log.info("Получен запрос POST по пути /requests для добавления запроса на вещь: {}", requestDto);
        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto);
        ItemRequest addedRequest = itemRequestService.save(request);
        return ItemRequestMapper.toItemRequestDto(addedRequest);
    }

    @PatchMapping("/{id}")
    public ItemRequestDto update(@PathVariable("id") Long id, @Valid @RequestBody ItemRequestDto requestDto) {
        log.info("Получен запрос PATCH по пути /requests для обновления запроса на вещь: {}", requestDto);
        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto);
        ItemRequest updatedRequest = itemRequestService.update(id, request);
        return ItemRequestMapper.toItemRequestDto(updatedRequest);
    }

    @GetMapping("/{id}")
    public ItemRequestDto getById(@PathVariable("id") Long id) {
        log.info("Получен запрос GET по пути /requests по id {}", id);
        return ItemRequestMapper.toItemRequestDto(itemRequestService.findById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Получен запрос DELETE по пути /requests по id {}", id);
        itemRequestService.delete(id);
    }
}
