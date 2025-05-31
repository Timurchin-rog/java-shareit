package ru.practicum.shareit.booking.model;

public enum BookingState {
    WAITING, //Ожидает одобрения
    APPROVED, //Подтверждено
    REJECTED, //Отклонено владельцем
    CANCELED //Отклонено создателем
}
