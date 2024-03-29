package com.example.mystoryapp.storyapp.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.R.string
import com.example.mystoryapp.model.Story
import com.example.mystoryapp.databinding.ActivityDetailStoryBinding
import com.example.mystoryapp.utils.ConstVal
import com.example.mystoryapp.utils.ext.setImageUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailStoryActivity : AppCompatActivity() {

    private var _activityDetailStoryBinding: ActivityDetailStoryBinding? = null
    private val binding get() = _activityDetailStoryBinding!!

    private lateinit var story: Story

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityDetailStoryBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(_activityDetailStoryBinding?.root)

        initIntent()
        initUI()
    }

    private fun initUI() {
        binding.apply {
            imgStoryThumbnail.setImageUrl(story.photoUrl, true)
            tvStoryTitle.text = story.name
            tvStoryDesc.text = story.description
        }
        title = getString(string.title_detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initIntent() {
        story = intent.getParcelableExtra(ConstVal.BUNDLE_KEY_STORY)!!
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }

}