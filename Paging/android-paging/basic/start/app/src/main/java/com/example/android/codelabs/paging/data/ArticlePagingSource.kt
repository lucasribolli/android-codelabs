package com.example.android.codelabs.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.math.max

private val firstArticleCreatedTime = LocalDateTime.now()
// The initial key used for loading.
// This is the article id of the first article that will be loaded
private const val STARTING_KEY = 0
private const val LOAD_DELAY_MILLIS = 3_000L

class ArticlePagingSource : PagingSource<Int, Article>() {
    /**
     * It will be called by the Paging library to asynchronously fetch more data to be displayed
     * as the user scrolls around.
     *
     * [params.key] key of the page to be loaded
     * [params.loadSize] number of items to load
     *
     * Returns a [LoadResult] that can represent success, error or invalidation
     *
     * [LoadResult.Page.data] is the List of the items fetched
     * [LoadResult.Page.prevKey] key to fetch items behind the current page
     * [LoadResult.Page.nextKey] key to fetch items after the current page
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        // Start paging with the STARTING_KEY if this is the first load
        val start = params.key ?: STARTING_KEY
        // Load as many items as hinted by params.loadSize
        val range = start.until(start + params.loadSize)

        // Adding a delay to see the progress in UI
        if(start != STARTING_KEY) delay(LOAD_DELAY_MILLIS)

        return LoadResult.Page(
            data = range.map { number ->
                Article(
                    // Generate consecutive increasing numbers as the article id
                    id = number,
                    title = "Article $number",
                    description = "This describes article $number",
                    created = firstArticleCreatedTime.minusDays(number.toLong())
                )
            },
            // Make sure we don't try to load items behind the STARTING_KEY
            prevKey = when (start) {
                STARTING_KEY -> null
                else -> ensureValidKey(range.first - params.loadSize)
            },
            // We support infinite items. In real world this will be null when the list reach its limit
            nextKey = range.last + 1
        )
    }


    /**
     * The refresh key is used for the initial load of the next PagingSource, after invalidation
     */
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // In our case we grab the item closest to the anchor position
        // then return its id -> (state.config.pageSize / 2) as a buffer
        val anchorPosition = state.anchorPosition ?: return null
        val article = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(article.id - (state.config.pageSize / 2))
    }

    /**
     * Makes sure the paging key is never less than [STARTING_KEY]
     */
    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}