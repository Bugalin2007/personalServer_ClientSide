package com.bugalin.handler;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 简单包装的文本编辑器 - 最容易使用
 */
public class SwingTextEditor extends JFrame {

    private static boolean initialized = false;

    /**
     * 初始化编辑器（必须在第一次使用前调用）
     */
    public static void init() {
        if (initialized) {
            return;
        }

        // 使用invokeAndWait确保EDT初始化完成
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 创建一个隐藏的JFrame来强制初始化EDT
                JFrame hiddenFrame = new JFrame();
                hiddenFrame.setUndecorated(true);
                hiddenFrame.setOpacity(0.0f);
                hiddenFrame.setVisible(true);
                hiddenFrame.dispose();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        initialized = true;
    }

    /**
     * 打开编辑器（同步，阻塞）
     */
    public static int edit(File file) {
        return edit(file.toPath());
    }

    /**
     * 打开编辑器（同步，阻塞）
     */
    public static int edit(String filePath) {
        return edit(Path.of(filePath));
    }

    public static int edit(Path filePath) {
        if (!initialized) {
            throw new IllegalStateException("请先调用SimpleEditor.init()初始化");
        }

        // 确保文件存在
        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.err.println("文件创建失败: " + e.getMessage());
            return -1;
        }

        // 使用CompletableFuture来处理异步结果
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            try {
                int result = createAndShowDialog(filePath);
                future.complete(result);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        try {
            return future.get(); // 阻塞直到完成
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int createAndShowDialog(Path filePath) {
        // 读取文件
        String content;
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            content = "";
        }

        // 创建文本区域
        JTextArea textArea = new JTextArea(20, 60);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setText(content);

        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(textArea);

        // 使用JOptionPane，它会自动处理模态对话框
        Object[] options = {"保存并关闭", "取消编辑并关闭"};

        int choice = JOptionPane.showOptionDialog(
                null, // 无父窗口
                scrollPane,
                "编辑: " + filePath.getFileName(),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) { // 保存按钮
            try {
                Files.writeString(filePath, textArea.getText());
                return 0;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "保存失败: " + e.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
                return -1;
            }
        } else { // 取消或关闭
            return 1;
        }
    }

    /**
     * 异步打开编辑器（不阻塞调用线程）
     */
    public static CompletableFuture<Integer> editAsync(File file) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                int result = edit(file);
                future.complete(result);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, "Editor-Async").start();

        return future;
    }
}