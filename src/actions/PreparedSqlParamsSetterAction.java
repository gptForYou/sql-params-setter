package actions;

import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import util.SqlUtil;
import util.StringUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import static constants.Const.SEPARATOR_PARAMETER;
import static constants.Const.SEPARATOR_PREPARING;

/**
 * @author yangchenwen
 * @since 2020-06-09 15:27:40
 */
public class PreparedSqlParamsSetterAction extends AnAction {

    private static final NotificationGroup NOTIFICATION_GROUP =
            NotificationGroupManager.getInstance().getNotificationGroup("SqlParamsSetter.NotificationGroup");

    @Override
    public void actionPerformed(AnActionEvent e) {

        final Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        Project project = editor.getProject();
        if (project == null) {
            return;
        }

        SelectionModel model = editor.getSelectionModel();
        final String selectedMybatisLogs = model.getSelectedText();
        if (StringUtil.isBlank(selectedMybatisLogs)) {
            return;
        }

        String sql;
        try {
            sql = SqlUtil.parse(selectedMybatisLogs);
        } catch (Exception ex) {
            notify(project, String.format("Failed at: %s", ex.toString()), NotificationType.ERROR);
            return;
        }

        if (StringUtil.isBlank(sql)) {
            notify(project, String.format(
                    "Selected area should contain both [%s] in the 1st line and [%s] in the 2nd line.",
                    SEPARATOR_PREPARING, SEPARATOR_PARAMETER), NotificationType.WARNING);
            return;
        }

        StringSelection selection = new StringSelection(sql);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        notify(project, "Success, copied to clipboard.", NotificationType.INFORMATION);
    }

    private void notify(Project project, String message, NotificationType type) {
        Notification success = NOTIFICATION_GROUP.createNotification(message, type);
        Notifications.Bus.notify(success, project);
    }
}