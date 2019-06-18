package com.melardev.spring.rest.dtos.responses


class PageMeta {
    var isHasNextPage: Boolean = false
        private set
    var hasPrevPage: Boolean = false
    var currentPageNumber: Int = 0
        private set
    private var totalItemsCount: Long = 0 // total cartItems in total
    var requestedPageSize: Int = 0 // max cartItems per page
    var currentItemsCount: Int = 0 // cartItems in this page
    var numberOfPages: Int = 0
        private set // number of pages
    var offset: Int = 0
    var nextPageNumber: Int = 0
    var prevPageNumber: Int = 0
        private set
    var nextPageUrl: String? = null
    var prevPageUrl: String? = null

    fun setTotalPageCount(pageCount: Int) {
        this.numberOfPages = pageCount
    }

    fun setTotalItemsCount(totalItemsCount: Long?) {
        this.totalItemsCount = totalItemsCount!!
    }

    fun getTotalItemsCount(): Long? {
        return totalItemsCount
    }

    fun setCurrentPage(currentPage: Int) {
        this.currentPageNumber = currentPage
    }

    fun isHasPrevPage(): Boolean {
        return hasPrevPage
    }

    fun getCurrentPage(): Int {
        return currentPageNumber
    }

    companion object {


        fun build(resources: Collection<*>, basePath: String, page: Int, pageSize: Int, totalItemsCount: Long): PageMeta {
            val pageMeta = PageMeta()
            pageMeta.offset = (page - 1) * pageSize
            pageMeta.requestedPageSize = pageSize
            pageMeta.currentItemsCount = resources.size
            pageMeta.setCurrentPage(page)


            pageMeta.setTotalItemsCount(totalItemsCount)
            pageMeta.setTotalPageCount(Math.ceil((pageMeta.getTotalItemsCount()!! / pageMeta.requestedPageSize).toDouble()).toInt())
            pageMeta.isHasNextPage = pageMeta.currentPageNumber < pageMeta.numberOfPages
            pageMeta.hasPrevPage = pageMeta.currentPageNumber > 1
            if (pageMeta.isHasNextPage) {
                pageMeta.nextPageNumber = pageMeta.currentPageNumber + 1
                pageMeta.nextPageUrl = String.format("%s?page_size=%d&page=%d",
                        basePath, pageMeta.requestedPageSize, pageMeta.nextPageNumber)
            } else {
                pageMeta.nextPageNumber = pageMeta.numberOfPages
                pageMeta.nextPageUrl = String.format("%s?page_size=%d&page=%d",
                        basePath, pageMeta.requestedPageSize, pageMeta.nextPageNumber)
            }

            if (pageMeta.hasPrevPage) {
                pageMeta.prevPageNumber = pageMeta.currentPageNumber - 1

                pageMeta.prevPageUrl = String.format("%s?page_size=%d&page=%d",
                        basePath, pageMeta.requestedPageSize,
                        pageMeta.prevPageNumber)
            } else {
                pageMeta.prevPageNumber = 1
                pageMeta.prevPageUrl = String.format("%s?page_size=%d&page=%d",
                        basePath, pageMeta.requestedPageSize, pageMeta.prevPageNumber)
            }

            return pageMeta

        }
    }

}
