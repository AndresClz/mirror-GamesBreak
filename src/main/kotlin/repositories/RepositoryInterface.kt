package repositories

interface RepositoryInterface<T> {
    fun getByID(id: String): T?
    fun getAll(): List<T>?
    fun addItem(item: T): Boolean
    fun removeItem(id: Long): Boolean
}