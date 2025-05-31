package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequestor_Id(long requestorId, Sort sort);

    List<Request> findAllByRequestor_IdNot(long requestorId, Sort sort);
}
