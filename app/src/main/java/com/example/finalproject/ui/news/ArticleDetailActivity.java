package com.example.finalproject.ui.news;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.ui.news.Article;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

public class ArticleDetailActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewContent, textViewSection, textViewDate, textViewUrl;
    private ImageView imageViewArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("News Details");
        }

        imageViewArticle = findViewById(R.id.imageArticle);
        textViewTitle = findViewById(R.id.articleTitle);
        textViewContent = findViewById(R.id.articleContent);
        textViewSection = findViewById(R.id.sectionArticle);
        textViewDate = findViewById(R.id.dateArticle);
        textViewUrl = findViewById(R.id.urlTextView);

        // Retrieve the article data from the intent
        Article article = (Article) getIntent().getSerializableExtra("ARTICLE_DATA");
        if (article != null) {
            textViewTitle.setText(article.getTitle());
            textViewContent.setText(article.getAbstract());
            textViewSection.setText("Section: "+ article.getSection());
            String formattedDate = formatDateString(article.getPublished_date());
            textViewDate.setText("Date: " + formattedDate);
            textViewUrl.setText(article.getUrl());
            // Make the URL clickable
            Linkify.addLinks(textViewUrl, Linkify.WEB_URLS);
            textViewUrl.setMovementMethod(LinkMovementMethod.getInstance());

            Log.d("ArticleDetailActivity", "Image URL: " + article.getImageUrl());


            // Load the image using Glide
            Glide.with(this)
                    .load(article.getImageUrl())
                    .into(imageViewArticle);
        }
        }


    private String formatDateString(String rawDate) {
        // Define the input and output date formats
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy, hh:mm a");

        try {
            // Parse the input date string
            Date date = inputFormat.parse(rawDate);
            // Format the date into the output format
            return outputFormat.format(date);
        } catch (ParseException e) {
            Log.e("ArticleDetailActivity", "Error parsing date", e);
            // Return the original date string if parsing fails
            return rawDate;
        }
    }

}


