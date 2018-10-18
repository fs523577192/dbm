package org.firas.common.bo

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
enum class CommonStatus(val desc: String) {
    DELETED("已删除"),
    NORMAL("正常"),
    INVALID("无效"),
    AUDITING("审核中"),
    EXPIRED("已过期"),
    EXAMINING("复核中"),
    NOT_BEGIN("未开始");

    fun toCode(): String {
        val code = "0${ordinal}"
        return code.subSequence(code.length - 2, code.length).toString()
    }
}