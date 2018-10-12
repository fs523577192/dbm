package org.firas.common.po

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月11日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
interface PoBase<BO, DTO> {

    fun toBO(): BO

    fun toDTO(): DTO
}