package org.firas.dbm.exception

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月18日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class EntityNotExistException: Exception {

    constructor(message: String = ""): super(message)

    constructor(message: String = "", cause: Throwable): super(message, cause)
}