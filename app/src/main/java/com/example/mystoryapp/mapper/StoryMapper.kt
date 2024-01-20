package com.example.mystoryapp.mapper

import com.example.mystoryapp.local.entity.StoryEntity
import com.example.mystoryapp.model.Story

fun storyToStoryEntity(story: Story): StoryEntity {
    return StoryEntity(
        id = story.id,
        photoUrl = story.photoUrl
    )
}