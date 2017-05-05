package com.evil.webbrowser.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * shell命令工具类
 */
public class ShellUtils {

    public static final String COMMAND_SU       = "su";
    public static final String COMMAND_SH       = "sh";
    public static final String COMMAND_EXIT     = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    /**
     * 检查是否已经root
     */
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    /**
     * 运行shell命令,返回结果消息
     * @param command 命令消息
     * @param isRoot 是否已经root
     */
    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[]{command}, isRoot, true);
    }

    /**
     * 运行shell命令,返回结果消息
     * @param commands 命令集合
     * @param isRoot 是否已经root
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        return execCommand(commands == null
                           ? null
                           : commands.toArray(new String[]{}), isRoot, true);
    }

    /**
     * 运行shell命令,返回结果消息
     * @param commands 命令数组
     * @param isRoot 是否已经root
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        return execCommand(commands, isRoot, true);
    }

    /**
     * 运行shell命令
     * @param command 命令
     * @param isRoot 是否已经root
     * @param isNeedResultMsg 是否需要结果消息
     */
    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg)
    {
        return execCommand(new String[]{command}, isRoot, isNeedResultMsg);
    }

    /**
     * 运行shell命令,返回结果消息
     * @param commands 命令数组
     * @param isRoot 是否已经root
     * @param isNeedResultMsg 是否返回消息
     */
    public static CommandResult execCommand(List<String> commands,
                                            boolean isRoot,
                                            boolean isNeedResultMsg)
    {
        return execCommand(commands == null
                           ? null
                           : commands.toArray(new String[]{}), isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     *
     * @param commands command array
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return <ul>
     *         <li>if isNeedResultMsg is false, {@link CommandResult#successMsg} is null and
     *         {@link CommandResult#errorMsg} is null.</li>
     *         <li>if {@link CommandResult#result} is -1, there maybe some excepiton.</li>
     *         </ul>
     */
    public static CommandResult execCommand(String[] commands,
                                            boolean isRoot,
                                            boolean isNeedResultMsg)
    {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process        process       = null;
        BufferedReader successResult = null;
        BufferedReader errorResult   = null;
        StringBuilder  successMsg    = null;
        StringBuilder  errorMsg      = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime()
                             .exec(isRoot
                                   ? COMMAND_SU
                                   : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            // get command result
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.close(os);
            FileUtils.close(successResult);
            FileUtils.close(errorResult);

            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result,
                                 successMsg == null
                                 ? null
                                 : successMsg.toString(),
                                 errorMsg == null
                                 ? null
                                 : errorMsg.toString());
    }

    /**
     * 命令结果
     */
    public static class CommandResult {

        /**
         * 结果,0为正常,否则错误
         */
        public int    result;
        /**
         * 正确的命令结果消息
         */
        public String successMsg;
        /**
         * 正确的命令结果消息
         */

        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
}
