package net.narutoxboruto.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.ReleaseList;
import net.narutoxboruto.main.Main;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ShinobiStatsGui extends Screen {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID,
            "textures/gui/shinobi_stats.png");

    private final String[] guiComponentsList = {
            "taijutsu", "ninjutsu", "genjutsu", "kenjutsu", "kinjutsu", "medical", "senjutsu", "shurikenjutsu", "speed",
            "summoning", "affiliation", "clan", "rank", "shinobi_points"
    };
    private final List<Integer> lockedList = new ArrayList<>(Arrays.asList(1, 2, 4, 6, 9));

    // Store the player reference
    private final LocalPlayer player;

    public ShinobiStatsGui() {
        super(Component.translatable("gui.narutoxboruto.shinobi_stats"));
        this.player = Minecraft.getInstance().player;
    }

    // Helper methods to get current values from attachments
    private int[] getValueIntList() {
        if (player == null) return new int[14];

        return new int[] {
                getTaijutsu(), getNinjutsu(), getGenjutsu(), getKenjutsu(),
                getKinjutsu(), getMedical(), getSenjutsu(), getShurikenjutsu(),
                getSpeed(), getSummoning(), 0, 0, 0, getShinobiPoints()
        };
    }

    private String[] getValueStringList() {
        return new String[] {
                getAffiliation(), getClan(), getRank()
        };
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderBackground(guiGraphics, pMouseX, pMouseY, pPartialTick);
        guiGraphics.blit(BACKGROUND, (this.width - 234) / 2, (this.height - 192) / 2, 0, 0, 256, 192);
        this.renderInfo(guiGraphics);
        this.drawReleaseIcons(guiGraphics, 95, 16);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    protected void createMenuControls() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose()).bounds((this.width - 157) / 2, this.height / 2 + 90, 150, 20).build());
    }

    public void renderInfo(GuiGraphics guiGraphics) {
        // Get current values each render to ensure they're up to date
        int[] currentIntValues = getValueIntList();
        String[] currentStringValues = getValueStringList();

        for (int l = 0; l < this.guiComponentsList.length; ++l) {
            if (!this.guiComponentsList[l].isBlank()) {
                if (l < 10) {
                    drawIntStat(guiGraphics, l, 0, 0, currentIntValues);
                }
                else if (l < 13) {
                    drawStringStat(guiGraphics, l, currentStringValues);
                    switch (l) {
                        case 10 -> drawIcon(guiGraphics, 147, 11, l, currentStringValues);
                        case 11 -> drawIcon(guiGraphics, 121, 11, l, currentStringValues);
                    }
                }
                else {
                    drawIntStat(guiGraphics, l, -1, 101, currentIntValues);
                }
            }
        }
    }

    private void drawIntStat(GuiGraphics guiGraphics, int index, int yOffset, int xOffset, int[] currentIntValues) {
        String value = this.lockedList.contains(index) && currentIntValues[index] == 0 ? "-" : String.valueOf(currentIntValues[index]);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(1.0f, 1.0f, 1.0f);

        guiGraphics.drawString(this.font, Component.translatable("shinobiStat." + this.guiComponentsList[index]).append(":"),
                ((this.width - 192) / 2 - 5) + xOffset, (this.height / 2 - 79 + (index + yOffset) * 12),
                0, false);
        guiGraphics.drawString(this.font, Component.literal(value), ((this.width - 192) / 2 + 72 + xOffset),
                (this.height / 2 - 79 + (index + yOffset) * 12), 0, false);

        guiGraphics.pose().popPose();
    }

    private void drawStringStat(GuiGraphics guiGraphics, int index, String[] currentStringValues) {
        String spaces = index == 12 ? "" : "   ";
        guiGraphics.drawString(this.font, Component.translatable("shinobiStat." + this.guiComponentsList[index])
                        .append(": " + spaces)
                        .append(Component.translatable(this.guiComponentsList[index] + "." + currentStringValues[index - 10])),
                ((this.width - 192) / 2 + 95), (this.height / 2 - 79 + (index - 10) * 12), 0, false);
    }

    private void drawIcon(GuiGraphics guiGraphics, int x, int size, int l, String[] currentStringValues) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID,
                "textures/" + this.guiComponentsList[l] + "s/" + currentStringValues[l - 10] + ".png");
        guiGraphics.blit(texture, (this.width - 192) / 2 + x, this.height / 2 - 80 + (l - 10) * 12, 0, 0, 0, size, size, size, size);
    }

    private void drawReleaseIcons(GuiGraphics guiGraphics, int x, int size) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        ReleaseList releaseListAttachment = player.getData(MainAttachment.RELEASE_LIST);
        Component releasesLabel = Component.translatable("shinobiStat.releases")
                .append(": ")
                .append(releaseListAttachment.isEmpty()
                        ? Component.translatable("shinobiStat.release_unknown")
                        : Component.empty());

        guiGraphics.drawString(this.font, releasesLabel, (this.width - 192) / 2 + x, this.height / 2 - 43, 0, false);

        if (!releaseListAttachment.isEmpty()) {
            List<String> releaseList = releaseListAttachment.getReleasesAsList();

            for (int l = 0; l < releaseList.size(); ++l) {
                int row = l < 6 ? 0 : 1;
                int column = l < 6 ? l : l - 6;

                String releaseName = releaseList.get(l).toLowerCase();
                ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID,
                        "textures/item/release/" + releaseName + ".png");

                guiGraphics.blit(texture,
                        (this.width - 192) / 2 + x + (size + 1) * column,
                        this.height / 2 - 33 + row * (size + 1),
                        0, 0, 0, size, size, size, size);
            }
        }
    }

    @Override
    protected void init() {
        createMenuControls();
        super.init();
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // Individual stat getters from attachments
    private int getTaijutsu() {
        return player.getData(MainAttachment.TAIJUTSU).getValue();
    }

    private int getNinjutsu() {
        return player.getData(MainAttachment.NINJUTSU).getValue();
    }

    private int getGenjutsu() {
        return player.getData(MainAttachment.GENJUTSU).getValue();
    }

    private int getKenjutsu() {
        return player.getData(MainAttachment.KENJUTSU).getValue();
    }

    private int getKinjutsu() {
        return player.getData(MainAttachment.KINJUTSU).getValue();
    }

    private int getMedical() {
        return player.getData(MainAttachment.MEDICAL).getValue();
    }

    private int getSenjutsu() {
        return player.getData(MainAttachment.SENJUTSU).getValue();
    }

    private int getShurikenjutsu() {
        return player.getData(MainAttachment.SHURIKENJUTSU).getValue();
    }

    private int getSpeed() {
        return player.getData(MainAttachment.SPEED).getValue();
    }

    private int getSummoning() {
        return player.getData(MainAttachment.SUMMONING).getValue();
    }

    private int getShinobiPoints() {
        return player.getData(MainAttachment.SHINOBI_POINTS).getValue();
    }

    private String getAffiliation() {
        return player.getData(MainAttachment.AFFILIATION).getValue();
    }

    private String getClan() {
        return player.getData(MainAttachment.CLAN).getValue();
    }

    private String getRank() {
        return player.getData(MainAttachment.RANK).getValue();
    }
}
