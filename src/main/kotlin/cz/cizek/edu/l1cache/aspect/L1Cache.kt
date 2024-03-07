package cz.cizek.edu.l1cache.aspect


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class L1Cache(val key: String) { //SPEL it ;)
}