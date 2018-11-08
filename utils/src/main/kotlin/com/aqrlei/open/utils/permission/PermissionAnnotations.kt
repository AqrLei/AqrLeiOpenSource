package  com.aqrlei.open.utils.permission

/**
 * @author aqrlei on 2018/10/15
 */

@Target(AnnotationTarget.FUNCTION)
@Retention
@MustBeDocumented
annotation class Required(val permissions: Array<String>)

@Target(AnnotationTarget.FUNCTION)
@Retention
@MustBeDocumented
annotation class OnPermissionDenied

@Target(AnnotationTarget.FUNCTION)
@Retention
@MustBeDocumented
annotation class OnPermissionGranted

@Target(AnnotationTarget.FUNCTION)
@Retention
@MustBeDocumented
annotation class OnPermissionRationale