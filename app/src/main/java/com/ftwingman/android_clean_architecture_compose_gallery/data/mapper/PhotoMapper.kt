package com.ftwingman.android_clean_architecture_compose_gallery.data.mapper

import com.ftwingman.android_clean_architecture_compose_gallery.data.local.entity.PhotoEntity
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.dto.PhotoDto
import com.ftwingman.android_clean_architecture_compose_gallery.model.ExifInfo
import com.ftwingman.android_clean_architecture_compose_gallery.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.model.User

fun PhotoDto.toDomain(): Photo {
    return Photo(
        id = id,
        width = width,
        height = height,
        url = urls.regular,
        thumbnailUrl = urls.small,
        color = color,
        blurHash = blurHash,
        description = description,
        author = User(
            id = user.id,
            username = user.username,
            name = user.name,
            profileImage = user.profileImage.medium
        ),
        exif = exif?.let {
            ExifInfo(
                make = it.make,
                model = it.model,
                exposureTime = it.exposureTime,
                aperture = it.aperture,
                focalLength = it.focalLength,
                iso = it.iso
            )
        },
        unsplashUrl = links.html,
        downloadUrl = links.download
    )
}

fun PhotoDto.toEntity(): PhotoEntity {
    return PhotoEntity(
        id = id,
        width = width,
        height = height,
        url = urls.regular,
        thumbnailUrl = urls.small,
        color = color,
        blurHash = blurHash,
        description = description,
        authorName = user.name,
        authorUsername = user.username,
        authorProfileImage = user.profileImage.medium,
        exifMake = exif?.make,
        exifModel = exif?.model,
        exifExposureTime = exif?.exposureTime,
        exifAperture = exif?.aperture,
        exifFocalLength = exif?.focalLength,
        exifIso = exif?.iso,
        unsplashUrl = links.html,
        downloadUrl = links.download
    )
}

fun PhotoEntity.toDomain(): Photo {
    return Photo(
        id = id,
        width = width,
        height = height,
        url = url,
        thumbnailUrl = thumbnailUrl,
        color = color,
        blurHash = blurHash,
        description = description,
        author = User(
            id = "", // Entity doesn't store author ID currently
            username = authorUsername,
            name = authorName,
            profileImage = authorProfileImage
        ),
        exif = ExifInfo(
            make = exifMake,
            model = exifModel,
            exposureTime = exifExposureTime,
            aperture = exifAperture,
            focalLength = exifFocalLength,
            iso = exifIso
        ),
        unsplashUrl = unsplashUrl,
        downloadUrl = downloadUrl
    )
}