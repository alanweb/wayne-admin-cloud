package com.wayne.auth.session;

import com.wayne.common.plugin.system.domain.SysBaseUser;
import com.wayne.common.web.session.HttpSessionContextHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Describe: Security Session 操作服务
 * Author: 就眠仪式
 * CreateTime: 2019/10/23
 */
public class SecureSessionService {

    public static void expiredSession(HttpServletRequest request, SessionRegistry sessionRegistry) {
        SysBaseUser currentUser = (SysBaseUser) request.getSession().getAttribute("currentUser");
        String sessionId = request.getSession().getId();
        // 从sessionRegistry中获取所有的用户信息
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            SysBaseUser userDetails = (SysBaseUser) principal;
            // 若sessionRegistry中的userDetails的用户名与当前用户的用户名相同
            if (userDetails != null && userDetails.getUsername().equals(currentUser.getUsername())) {
                // 查询用户所有的SessionInformation，包括过期session
                List<SessionInformation> sessionInformationList = sessionRegistry.getAllSessions(userDetails, true);
                if (CollectionUtils.isNotEmpty(sessionInformationList)) {
                    for (SessionInformation sessionInformation : sessionInformationList) {
                        // 不处理当前用户的session
                        if (sessionId.equals(sessionInformation.getSessionId())) {
                            continue;
                        }
                        // 从sessionRegistry中清除session信息
                        sessionInformation.expireNow();
                        sessionRegistry.removeSessionInformation(sessionInformation.getSessionId());
                        // 从HttpSessionContext中清除session信息
                        HttpSession httpSession = HttpSessionContextHolder.currentSessionContext().getSession(sessionInformation.getSessionId());
                        HttpSessionContextHolder.currentSessionContext().removeSession(httpSession);
                    }
                }
            }
        }
    }
}
