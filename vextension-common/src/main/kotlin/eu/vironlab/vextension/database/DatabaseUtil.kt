package eu.vironlab.vextension.database

import com.google.inject.Inject
import eu.vironlab.vextension.database.annotation.ORMKey
import eu.vironlab.vextension.database.annotation.ORMObject

object DatabaseUtil {

    @JvmStatic
    fun validateOrmClass(ormClass: Class<*>): ORMValidationResult {
        if (!ormClass.isAnnotationPresent(ORMObject::class.java)) {
            return ORMValidationResult.NO_ORM
        }
        var injectConstructorPresent: Boolean = false
        for (constructor in ormClass.declaredConstructors) {
            if (constructor.isAnnotationPresent(Inject::class.java)) {
                injectConstructorPresent = true
            }
        }
        if (!injectConstructorPresent) {
            return ORMValidationResult.INJECT_MISSING
        }
        var key: Boolean = false
        if (ormClass.fields.filter { it.isAnnotationPresent(ORMKey::class.java) }.size != 1) {
            return ORMValidationResult.INVALID_KEY
        }
        return ORMValidationResult.OK
    }

}

enum class ORMValidationResult(val success: Boolean, val message: String) {
    NO_ORM(false, "There is no ORMObject Annotation"),
    INJECT_MISSING(false, "There is no Inject Constructor to creating an instance"),
    INVALID_KEY(false, "You need exact 1 key in the ORM Object"),
    OK(true, "OK"),
    KEY_CANNOT_IGNORED(false, "The Key cannot be ignored")
}