package ru.mal.unialbumsbackend.web.mappers;

public interface Mappable<E,D>{

    E toEntity(D dto);

    D toDto(E entity);

}
