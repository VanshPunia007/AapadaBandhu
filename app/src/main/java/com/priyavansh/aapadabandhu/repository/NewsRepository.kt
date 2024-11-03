package com.priyavansh.aapadabandhu.repository

import com.priyavansh.aapadabandhu.db.ArticleDatabase
import com.priyavansh.aapadabandhu.api.RetrofitInstance
import com.priyavansh.aapadabandhu.models.Article


class NewsRepository(val db : ArticleDatabase) {
    suspend fun getHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article : Article) = db.getArticleDao().upsert(article)

    fun getFavouriteNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}