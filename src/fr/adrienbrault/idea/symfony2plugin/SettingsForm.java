package fr.adrienbrault.idea.symfony2plugin;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SettingsForm implements Configurable {

    private Project project;

    private JPanel panel1;
    private TextFieldWithBrowseButton pathToContainerTextField;
    private JButton pathToContainerTextFieldReset;
    private JButton pathToUrlGeneratorTextFieldReset;

    private JCheckBox symfonyContainerTypeProvider;
    private JCheckBox objectRepositoryTypeProvider;
    private JCheckBox objectRepositoryResultTypeProvider;

    private TextFieldWithBrowseButton pathToUrlGeneratorTextField;
    private JLabel typesLabel;
    private JTextField phpTypesLifetimeSec;
    private JCheckBox twigAnnotateRoute;
    private JCheckBox twigAnnotateTemplate;
    private JCheckBox twigAnnotateAsset;
    private JCheckBox twigAnnotateAssetTags;
    private JCheckBox phpAnnotateTemplate;
    private JCheckBox phpAnnotateService;
    private JCheckBox phpAnnotateRoute;
    private JCheckBox phpAnnotateTemplateAnnotation;
    private JCheckBox pluginEnabled;
    private JCheckBox yamlAnnotateServiceConfig;

    public SettingsForm(@NotNull final Project project) {
        this.project = project;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Symfony2 Plugin";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    public JComponent createComponent() {

        pathToContainerTextField.getButton().addMouseListener(createPathButtonMouseListener(pathToContainerTextField.getTextField()));
        pathToContainerTextFieldReset.addMouseListener(createResetPathButtonMouseListener(pathToContainerTextField.getTextField(), Settings.DEFAULT_CONTAINER_PATH));

        pathToUrlGeneratorTextField.getButton().addMouseListener(createPathButtonMouseListener(pathToUrlGeneratorTextField.getTextField()));
        pathToUrlGeneratorTextFieldReset.addMouseListener(createResetPathButtonMouseListener(pathToUrlGeneratorTextField.getTextField(), Settings.DEFAULT_URL_GENERATOR_PATH));

        return (JComponent) panel1;
    }

    @Override
    public boolean isModified() {
        return
            !pluginEnabled.isSelected() == getSettings().pluginEnabled
            || !pathToContainerTextField.getText().equals(getSettings().pathToProjectContainer)
            || !pathToUrlGeneratorTextField.getText().equals(getSettings().pathToUrlGenerator)
            || !symfonyContainerTypeProvider.isSelected() == getSettings().symfonyContainerTypeProvider
            || !objectRepositoryTypeProvider.isSelected() == getSettings().objectRepositoryTypeProvider
            || !objectRepositoryResultTypeProvider.isSelected() == getSettings().objectRepositoryResultTypeProvider

            || !twigAnnotateRoute.isSelected() == getSettings().twigAnnotateRoute
            || !twigAnnotateTemplate.isSelected() == getSettings().twigAnnotateTemplate
            || !twigAnnotateAsset.isSelected() == getSettings().twigAnnotateAsset
            || !twigAnnotateAssetTags.isSelected() == getSettings().twigAnnotateAssetTags

            || !phpAnnotateTemplate.isSelected() == getSettings().phpAnnotateTemplate
            || !phpAnnotateService.isSelected() == getSettings().phpAnnotateService
            || !phpAnnotateRoute.isSelected() == getSettings().phpAnnotateRoute
            || !phpAnnotateTemplateAnnotation.isSelected() == getSettings().phpAnnotateTemplateAnnotation

            || !yamlAnnotateServiceConfig.isSelected() == getSettings().yamlAnnotateServiceConfig
        ;
    }

    @Override
    public void apply() throws ConfigurationException {

        getSettings().pluginEnabled = pluginEnabled.isSelected();

        getSettings().pathToProjectContainer = pathToContainerTextField.getText();
        getSettings().pathToUrlGenerator = pathToUrlGeneratorTextField.getText();

        getSettings().symfonyContainerTypeProvider = symfonyContainerTypeProvider.isSelected();
        getSettings().objectRepositoryTypeProvider = objectRepositoryTypeProvider.isSelected();
        getSettings().objectRepositoryResultTypeProvider = objectRepositoryResultTypeProvider.isSelected();

        getSettings().twigAnnotateRoute = twigAnnotateRoute.isSelected();
        getSettings().twigAnnotateTemplate = twigAnnotateTemplate.isSelected();
        getSettings().twigAnnotateAsset = twigAnnotateAsset.isSelected();
        getSettings().twigAnnotateAssetTags = twigAnnotateAssetTags.isSelected();

        getSettings().phpAnnotateTemplate = phpAnnotateTemplate.isSelected();
        getSettings().phpAnnotateService = phpAnnotateService.isSelected();
        getSettings().phpAnnotateRoute = phpAnnotateRoute.isSelected();
        getSettings().phpAnnotateTemplateAnnotation = phpAnnotateTemplateAnnotation.isSelected();

        getSettings().yamlAnnotateServiceConfig = yamlAnnotateServiceConfig.isSelected();
    }

    @Override
    public void reset() {
        updateUIFromSettings();
    }

    @Override
    public void disposeUIResources() {
    }

    private Settings getSettings() {
        return Settings.getInstance(project);
    }

    private void updateUIFromSettings() {

        pluginEnabled.setSelected(getSettings().pluginEnabled);

        pathToContainerTextField.setText(getSettings().pathToProjectContainer);
        pathToUrlGeneratorTextField.setText(getSettings().pathToUrlGenerator);

        symfonyContainerTypeProvider.setSelected(getSettings().symfonyContainerTypeProvider);
        objectRepositoryTypeProvider.setSelected(getSettings().objectRepositoryTypeProvider);
        objectRepositoryResultTypeProvider.setSelected(getSettings().objectRepositoryResultTypeProvider);

        twigAnnotateRoute.setSelected(getSettings().twigAnnotateRoute);
        twigAnnotateTemplate.setSelected(getSettings().twigAnnotateTemplate);
        twigAnnotateAsset.setSelected(getSettings().twigAnnotateAsset);
        twigAnnotateAssetTags.setSelected(getSettings().twigAnnotateAssetTags);

        phpAnnotateTemplate.setSelected(getSettings().phpAnnotateTemplate);
        phpAnnotateService.setSelected(getSettings().phpAnnotateService);
        phpAnnotateRoute.setSelected(getSettings().phpAnnotateRoute);
        phpAnnotateTemplateAnnotation.setSelected(getSettings().phpAnnotateTemplateAnnotation);

        yamlAnnotateServiceConfig.setSelected(getSettings().yamlAnnotateServiceConfig);
    }

    private MouseListener createPathButtonMouseListener(final JTextField textField) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                VirtualFile projectDirectory = project.getBaseDir();
                VirtualFile selectedFile = FileChooser.chooseFile(
                    FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
                    project,
                    VfsUtil.findRelativeFile(textField.getText(), projectDirectory)
                );

                if (null == selectedFile) {
                    return; // Ignore but keep the previous path
                }

                String path = VfsUtil.getRelativePath(selectedFile, projectDirectory, '/');
                if (null == path) {
                    path = selectedFile.getPath();
                }

                textField.setText(path);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        };
    }

    private MouseListener createResetPathButtonMouseListener(final JTextField textField, final String defaultValue) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                textField.setText(defaultValue);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        };
    }

}
