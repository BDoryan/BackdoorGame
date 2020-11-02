package isotopestudio.backdoor.game.applications;

import java.util.Random;

import org.joml.Vector4f;
import org.liquidengine.legui.animation.Animation;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.SplitPanel;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.component.event.label.LabelContentChangeEvent;
import org.liquidengine.legui.component.event.label.LabelContentChangeEventListener;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEvent;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEventListener;
import org.liquidengine.legui.component.event.textinput.TextInputContentChangeEvent;
import org.liquidengine.legui.component.event.textinput.TextInputContentChangeEventListener;
import org.liquidengine.legui.component.optional.Orientation;
import org.liquidengine.legui.component.optional.TextState;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.component.optional.align.VerticalAlign;
import org.liquidengine.legui.event.CharEvent;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.input.Mouse.MouseButton;
import org.liquidengine.legui.listener.CharEventListener;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.listener.processor.EventProcessorProvider;
import org.liquidengine.legui.style.Background;
import org.liquidengine.legui.style.Style.DisplayType;
import org.liquidengine.legui.style.Style.PositionType;
import org.liquidengine.legui.style.border.SimpleLineBorder;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.style.length.Auto;
import org.liquidengine.legui.util.TextUtil;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.core.gamescript.GameScript;
import isotopestudio.backdoor.core.gamescript.GameScript.GameScriptType;
import isotopestudio.backdoor.core.gamescript.GameScript.GameScripts;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.engine.components.desktop.Text;
import isotopestudio.backdoor.engine.components.desktop.TextField;
import isotopestudio.backdoor.engine.components.desktop.scrollablepanel.ScrollablePanel;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.engine.components.painting.Line;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.network.packet.packets.PacketPlayerBuyGameScript;

public class MarketApplication extends Window implements IComponent {

	public static MarketApplication main;

	public static void showApplication() {
		if (main == null) {
			main = new MarketApplication();
			BackdoorGame.getDesktop().addWindow(main);
			main.centerLocation();
			main.load();
		} else {
			if (BackdoorGame.getDesktop().containsWindow(main)) {
				main.showWindow();
				main.load();
			} else {
				main = null;
				showApplication();
			}
		}
	}

	public MarketApplication() {
		super("Market", 0, 0, 700, 500);
		getStyle().setMinimumSize(getSize().x, getSize().y);

		initMarket();
	}

	private void initMarket() {
		setVariable("market_window");

		getContainer().getStyle().setDisplay(DisplayType.FLEX);

		getContainer().add(initRight());
		getContainer().add(initTop());
		getContainer().add(initCenter());

		load();
	}

	private Panel top_content = new Panel();

	private Label money_label = new Label();

	private Panel initTop() {
		initContent(top_content);
		top_content.getStyle().setTop(((SimpleLineBorder) top_content.getStyle().getBorder()).getThickness());
		top_content.getStyle().setRight((float) right_content.getStyle().getWidth().get()
				+ (((SimpleLineBorder) top_content.getStyle().getBorder()).getThickness() * 2));
		top_content.getStyle().setLeft(((SimpleLineBorder) top_content.getStyle().getBorder()).getThickness());
		top_content.getStyle().setHeight(40f);

		money_label = new Label("");
		money_label.setVariable(getComponentVariable() + "_money_label");
		money_label.getStyle().setFontSize(24f);
		money_label.getStyle().setPosition(PositionType.RELATIVE);
		money_label.getStyle().setMarginLeft(Auto.AUTO);
		money_label.getStyle().setMarginRight(Auto.AUTO);
		money_label.getStyle().setMarginTop(Auto.AUTO);
		money_label.getStyle().setMarginBottom(Auto.AUTO);
		money_label.getListenerMap().addListener(LabelContentChangeEvent.class, new LabelContentChangeEventListener() {
			@Override
			public void process(LabelContentChangeEvent event) {
				money_label.getStyle().setMinWidth(money_label.getTextState().getTextWidth());
			}
		});

		top_content.add(money_label);

		return top_content;
	}

	private Panel right_content = new Panel();

	private Panel initRight() {
		initContent(right_content);
		right_content.getStyle().setTop(((SimpleLineBorder) right_content.getStyle().getBorder()).getThickness());
		right_content.getStyle().setRight(((SimpleLineBorder) right_content.getStyle().getBorder()).getThickness());
		right_content.getStyle().setBottom(((SimpleLineBorder) right_content.getStyle().getBorder()).getThickness());
		right_content.getStyle().setWidth(250f);

		Line title_line = new Line(1, 10, 40, (float) right_content.getStyle().getWidth().get() - 20f, 40);
		title_line.setColor(ColorConstants.white());

		right_content.add(script_name_label);
		right_content.add(title_line);
		right_content.add(script_description_text);
		right_content.add(description_label);
		right_content.add(buy_button);
		right_content.add(script_total_price_label);
		description_label.getStyle().setDisplay(DisplayType.NONE);

		script_amounts_panel.add(script_up_button_label);
		script_amounts_panel.add(script_down_button_label);
		script_amounts_panel.add(script_amount_textfield);

		right_content.add(script_amounts_panel);

		script_name_label.getListenerMap().addListener(LabelWidthChangeEvent.class,
				new LabelWidthChangeEventListener() {
					@Override
					public void process(LabelWidthChangeEvent event) {
						script_name_label.getStyle().setMinWidth(script_name_label.getTextState().getTextWidth());
					}
				});

		script_amount_textfield.getListenerMap().getListeners(CharEvent.class).clear();
		script_amount_textfield.getListenerMap().addListener(CharEvent.class, new CharEventListener() {
			public boolean isInteger(String s, int radix) {
				if (s.isEmpty())
					return false;
				for (int i = 0; i < s.length(); i++) {
					if (i == 0 && s.charAt(i) == '-') {
						if (s.length() == 1)
							return false;
						else
							continue;
					}
					if (Character.digit(s.charAt(i), radix) < 0)
						return false;
				}
				return true;
			}

			@Override
			public void process(CharEvent event) {
				TextInput textInput = (TextInput) event.getTargetComponent();
				if (textInput.isFocused() && textInput.isEditable() && !MouseButton.MOUSE_BUTTON_LEFT.isPressed()) {
					String str = TextUtil.cpToStr(event.getCodepoint());
					if (!isInteger(str, 10)) {
						return;
					}

					TextState textState = textInput.getTextState();
					String oldText = textState.getText();
					int start = textInput.getStartSelectionIndex();
					int end = textInput.getEndSelectionIndex();
					if (start > end) {
						start = textInput.getEndSelectionIndex();
						end = textInput.getStartSelectionIndex();
					}
					if (start != end) {
						StringBuilder t = new StringBuilder(textState.getText());
						t.delete(start, end);
						textState.setText(t.toString());
						textInput.setCaretPosition(start);
						textInput.setStartSelectionIndex(start);
						textInput.setEndSelectionIndex(start);
					}
					int caretPosition = textInput.getCaretPosition();
					StringBuilder t = new StringBuilder(textState.getText());
					t.insert(caretPosition, str);

					textState.setText(t.toString());
					int newCaretPosition = caretPosition + str.length();
					textInput.setCaretPosition(newCaretPosition);
					textInput.setEndSelectionIndex(newCaretPosition);
					textInput.setStartSelectionIndex(newCaretPosition);
					String newText = textState.getText();

					EventProcessorProvider.getInstance().pushEvent(new TextInputContentChangeEvent(textInput,
							event.getContext(), event.getFrame(), oldText, newText));
				}
			}
		});

		return right_content;
	}

	private Label script_name_label = new Label("");
	private Text script_description_text = new Text("");
	private Label description_label = new Label("");

	private Label script_total_price_label = new Label("");

	private Label script_up_button_label = new Label("");
	private TextField script_amount_textfield = new TextField("");
	private Label script_down_button_label = new Label("");

	private Panel script_amounts_panel = new Panel();

	private Button buy_button = new Button("");

	private MouseClickEventListener click = null;

	private void loadScript(GameScript gameScript) {
		script_name_label.getTextState().setText(gameScript.getName());
		script_name_label.setVariable(getComponentVariable() + "_script_name_label");
		script_name_label.getStyle().setFontSize(20f);
		script_name_label.getStyle().setPosition(PositionType.RELATIVE);
		script_name_label.getStyle().setMarginLeft(Auto.AUTO);
		script_name_label.getStyle().setMarginRight(Auto.AUTO);
		script_name_label.getStyle().setTop(10f);
		script_name_label.load();

		script_amounts_panel.getStyle().setDisplay(DisplayType.FLEX);
		script_amounts_panel.getStyle().setBorder(null);
		script_amounts_panel.getStyle().setShadow(null);
		script_amounts_panel.getStyle().getBackground().setColor(0, 0, 0, 0);
		script_amounts_panel.getStyle().setBottom(90f);
		script_amounts_panel.getStyle().setRight(80f);
		script_amounts_panel.getStyle().setLeft(80f);
		script_amounts_panel.getStyle().setHeight(20f);

		script_up_button_label.getTextState().setText("+");
		script_up_button_label.autoSizeWidth();
		script_up_button_label.setVariable(getComponentVariable() + "_script_up_button_label");
		script_up_button_label.getStyle().setFontSize(20f);
		script_up_button_label.getStyle().setLeft(10f);
		script_up_button_label.getStyle().setTop(0f);
		script_up_button_label.getStyle().setHeight(20f);
		script_up_button_label.getStyle().setMinHeight(20f);
		script_up_button_label.load();

		script_down_button_label.getTextState().setText("-");
		script_down_button_label.autoSizeWidth();
		script_down_button_label.setVariable(getComponentVariable() + "_script_down_button_label");
		script_down_button_label.getStyle().setFontSize(20f);
		script_down_button_label.getStyle().setRight(10f);
		script_down_button_label.getStyle().setTop(0f);
		script_down_button_label.getStyle().setHeight(20f);
		script_down_button_label.getStyle().setMinHeight(20f);
		script_down_button_label.load();

		script_amount_textfield.getTextState().setText("1");
		script_amount_textfield.setVariable(getComponentVariable() + "_script_amount_textfield");
		script_amount_textfield.getStyle().setHorizontalAlign(HorizontalAlign.CENTER);
		script_amount_textfield.getStyle().setBorder(null);
		script_amount_textfield.getStyle().setShadow(null);
		script_amount_textfield.setVariable(getComponentVariable() + "_script_amount_textfield");
		script_amount_textfield.getStyle().setFontSize(20f);
		script_amount_textfield.getStyle().setRight(25f);
		script_amount_textfield.getStyle().setLeft(25f);
		script_amount_textfield.getStyle().setBottom(0f);
		script_amount_textfield.getStyle().setTop(0f);
		script_amount_textfield.load();

		script_up_button_label.getListenerMap().removeAllListeners(MouseClickEvent.class);
		script_up_button_label.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (script_amount_textfield.getTextState().getText() != null
						&& script_amount_textfield.getTextState().getText().length() < 1)
					script_amount_textfield.getTextState().setText("1");
				int amount = Integer.valueOf(script_amount_textfield.getTextState().getText());
				if (event.getAction() == MouseClickAction.PRESS) {
					amount++;

					double cost_price = (gameScript.getPrice() * amount);

					if (cost_price > BackdoorGame.getGameClient().getMoney()) {
						buy_button.setEnabled(false);
					} else {
						buy_button.setEnabled(true);
					}

					script_amount_textfield.getTextState().setText((amount) + "");
					script_total_price_label.getTextState()
							.setText(Lang.get("market_full_cost", "%cost%", (cost_price) + ""));
				}
			}
		});

		script_down_button_label.getListenerMap().removeAllListeners(MouseClickEvent.class);
		script_down_button_label.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (script_amount_textfield.getTextState().getText() != null
						&& script_amount_textfield.getTextState().getText().length() < 1)
					script_amount_textfield.getTextState().setText("1");
				int amount = Integer.valueOf(script_amount_textfield.getTextState().getText());
				if (event.getAction() == MouseClickAction.PRESS && amount > 1) {
					amount--;
					script_amount_textfield.getTextState().setText(amount + "");

					double cost_price = (gameScript.getPrice() * amount);

					if (cost_price > BackdoorGame.getGameClient().getMoney()) {
						buy_button.setEnabled(false);
					} else {
						buy_button.setEnabled(true);
					}

					script_total_price_label.getTextState()
							.setText(Lang.get("market_full_cost", "%cost%", cost_price + ""));
				}
			}
		});

		script_amount_textfield.getListenerMap().removeAllListeners(TextInputContentChangeEvent.class);
		script_amount_textfield.getListenerMap().addListener(TextInputContentChangeEvent.class,
				new TextInputContentChangeEventListener() {
					@Override
					public void process(TextInputContentChangeEvent event) {
						if (script_amount_textfield.getTextState().getText().length() == 0) {
							script_amount_textfield.getTextState().setText("1");
							script_amount_textfield.setCaretPosition(1);
							return;
						}
						int amount = Integer.valueOf(script_amount_textfield.getTextState().getText());

						double cost_price = (gameScript.getPrice() * amount);

						if (cost_price > BackdoorGame.getGameClient().getMoney()) {
							buy_button.setEnabled(false);
						} else {
							buy_button.setEnabled(true);
						}
						script_total_price_label.getTextState()
								.setText(Lang.get("market_full_cost", "%cost%", (cost_price) + ""));
					}
				});

		script_total_price_label.getTextState()
				.setText(Lang.get("market_full_cost", "%cost%", gameScript.getPrice() + "") + "");
		script_total_price_label.setVariable(getComponentVariable() + "_script_total_price_label");
		script_total_price_label.getStyle().setFontSize(20f);
		script_total_price_label.getStyle().setBottom(60f);
		script_total_price_label.getStyle().setLeft(10f);
		script_total_price_label.load();

		description_label.setVariable(getComponentVariable() + "_description_label");
		description_label.getStyle().setFontSize(16f);
		description_label.getStyle().setTop(50f);
		description_label.getStyle().setLeft(10f);
		description_label.getStyle().setDisplay(DisplayType.MANUAL);
		description_label.load();

		script_description_text.getTextState().setText(gameScript.getDescriptionTranslated());
		script_description_text.setVariable(getComponentVariable() + "_script_description_text");
		script_description_text.getStyle().setFontSize(18f);
		script_description_text.getStyle().setVerticalAlign(VerticalAlign.TOP);
		script_description_text.getStyle().setTop(80f);
		script_description_text.getStyle().setLeft(10f);
		script_description_text.getStyle().setRight(10f);
		script_description_text.getStyle().setBottom(100f);
		script_description_text.getStyle().setBorderRadius(0F);
		script_description_text.load();

		buy_button.setVariable(getComponentVariable() + "_buy_button");
		buy_button.getStyle().setFontSize(22f);
		buy_button.getStyle().setLeft(10f);
		buy_button.getStyle().setRight(10f);
		buy_button.getStyle().setBottom(10f);
		buy_button.getStyle().setHeight(40f);
		buy_button.getStyle().setShadow(null);
		buy_button.getListenerMap().removeAllListeners(MouseClickEvent.class);
		buy_button.getListenerMap().addListener(MouseClickEvent.class, click = new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (event.getAction() != MouseClickAction.RELEASE)
					return;
				if(!buy_button.isEnabled())return;
				BackdoorGame.getGameClient().sendPacket(new PacketPlayerBuyGameScript(gameScript));
				script_amount_textfield.getTextState().setText("1");

				if (gameScript.getPrice() > BackdoorGame.getGameClient().getMoney()) {
					buy_button.setEnabled(false);
				}
			}
		});
		buy_button.load();

		if (gameScript.getPrice() > BackdoorGame.getGameClient().getMoney()) {
			buy_button.setEnabled(false);
		}

		description_label.getTextState().setText(Lang.get("market_description"));
		description_label.load();

		script_name_label.load();

		script_description_text.load();

		buy_button.getTextState().setText(Lang.get("market_buy_button"));
		buy_button.load();
	}

	private SplitPanel center_content = new SplitPanel(Orientation.HORIZONTAL);

	private Panel center_left_content = (Panel) center_content.getTopLeft();
	private Panel center_right_content = (Panel) center_content.getBottomRight();

	private Label offensive_script_label = new Label("");
	private Label defensive_script_label = new Label("");

	private ScrollablePanel scrollable_panel_offensive_script = new ScrollablePanel();
	private ScrollablePanel scrollable_panel_defensive_script = new ScrollablePanel();

	private Panel initCenter() {
		initContent(center_content);
		center_content.getStyle().setTop((float) top_content.getStyle().getHeight().get()
				+ (((SimpleLineBorder) top_content.getStyle().getBorder()).getThickness() * 2));
		center_content.getStyle().setRight((float) right_content.getStyle().getWidth().get()
				+ (((SimpleLineBorder) top_content.getStyle().getBorder()).getThickness() * 2));
		center_content.getStyle().setLeft(((SimpleLineBorder) center_content.getStyle().getBorder()).getThickness());
		center_content.getStyle().setBottom(((SimpleLineBorder) center_content.getStyle().getBorder()).getThickness());
		center_content.remove(center_content.getSeparator());

		initContent(center_left_content);
		initContent(center_right_content);

		center_left_content.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 1), 1f));
		center_right_content.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 1), 1f));

		offensive_script_label.setVariable(getComponentVariable() + "_offensive_script_label");
		offensive_script_label.getStyle().setFontSize(18f);
		offensive_script_label.getStyle().setPosition(PositionType.RELATIVE);
		offensive_script_label.getStyle().setMarginLeft(Auto.AUTO);
		offensive_script_label.getStyle().setMarginRight(Auto.AUTO);
		offensive_script_label.getStyle().setTop(10f);
		offensive_script_label.getListenerMap().addListener(LabelContentChangeEvent.class, e -> offensive_script_label
				.getStyle().setMinWidth(offensive_script_label.getTextState().getTextWidth()));

		center_left_content.add(offensive_script_label);

		scrollable_panel_offensive_script.setVariable(getComponentVariable() + "_scrollable_panel_offensive_script");
		scrollable_panel_offensive_script.getStyle().setLeft(10f);
		scrollable_panel_offensive_script.getStyle().setRight(10f);
		scrollable_panel_offensive_script.getStyle().setTop(30f);
		scrollable_panel_offensive_script.getStyle().setBottom(10f);
		scrollable_panel_offensive_script.getStyle().setShadow(null);
		scrollable_panel_offensive_script.getStyle().setBorder(null);
		scrollable_panel_offensive_script.getStyle().setBorderRadius(0f);

		scrollable_panel_offensive_script.getContainer().getStyle().setShadow(null);
		scrollable_panel_offensive_script.getContainer().getStyle().setBorder(null);
		scrollable_panel_offensive_script.getContainer().getStyle().setBorderRadius(0f);
		scrollable_panel_offensive_script.getContainer().getStyle().setDisplay(DisplayType.FLEX);
		scrollable_panel_offensive_script.getContainer().getStyle().setLeft(0f);
		scrollable_panel_offensive_script.getContainer().getStyle().setRight(0f);
		scrollable_panel_offensive_script.getContainer().getStyle().setTop(0f);

		scrollable_panel_offensive_script.getViewport().getStyle().setShadow(null);
		scrollable_panel_offensive_script.getViewport().getStyle().setBorder(null);
		scrollable_panel_offensive_script.getViewport().getStyle().setBorderRadius(0f);

		scrollable_panel_offensive_script.setAutoResize(true);
		scrollable_panel_offensive_script.setHorizontalScrollBarVisible(false);

		center_left_content.add(scrollable_panel_offensive_script);

		defensive_script_label.setVariable(getComponentVariable() + "_defensive_script_label");
		defensive_script_label.getStyle().setFontSize(18f);
		defensive_script_label.getStyle().setPosition(PositionType.RELATIVE);
		defensive_script_label.getStyle().setMarginLeft(Auto.AUTO);
		defensive_script_label.getStyle().setMarginRight(Auto.AUTO);
		defensive_script_label.getStyle().setTop(10f);
		defensive_script_label.getListenerMap().addListener(LabelContentChangeEvent.class, e -> defensive_script_label
				.getStyle().setMinWidth(defensive_script_label.getTextState().getTextWidth()));

		center_right_content.add(defensive_script_label);

		scrollable_panel_defensive_script.setVariable(getComponentVariable() + "_scrollable_panel_defensive_script");
		scrollable_panel_defensive_script.getStyle().setLeft(10f);
		scrollable_panel_defensive_script.getStyle().setRight(10f);
		scrollable_panel_defensive_script.getStyle().setTop(30f);
		scrollable_panel_defensive_script.getStyle().setBottom(10f);
		scrollable_panel_defensive_script.getStyle().setShadow(null);
		scrollable_panel_defensive_script.getStyle().setBorder(null);
		scrollable_panel_defensive_script.getStyle().setBorderRadius(0f);
		scrollable_panel_defensive_script.getStyle().getBackground().setColor(0, 0, 0, 0f);

		scrollable_panel_defensive_script.getContainer().getStyle().setShadow(null);
		scrollable_panel_defensive_script.getContainer().getStyle().setBorder(null);
		scrollable_panel_defensive_script.getContainer().getStyle().setBorderRadius(0f);
		scrollable_panel_defensive_script.getContainer().getStyle().setDisplay(DisplayType.FLEX);
		scrollable_panel_defensive_script.getContainer().getStyle().setLeft(0f);
		scrollable_panel_defensive_script.getContainer().getStyle().setRight(0f);
		scrollable_panel_defensive_script.getContainer().getStyle().setTop(0f);
		scrollable_panel_defensive_script.getContainer().getStyle().getBackground().setColor(0, 0, 0, 0f);

		scrollable_panel_defensive_script.getViewport().getStyle().setShadow(null);
		scrollable_panel_defensive_script.getViewport().getStyle().setBorder(null);
		scrollable_panel_defensive_script.getViewport().getStyle().setBorderRadius(0f);
		scrollable_panel_defensive_script.getViewport().getStyle().getBackground().setColor(0, 0, 0, 0f);

		scrollable_panel_defensive_script.setAutoResize(true);
		scrollable_panel_defensive_script.setHorizontalScrollBarVisible(false);

		center_right_content.add(scrollable_panel_defensive_script);

		return center_content;
	}

	private void initContent(Panel content) {
		content.getStyle().setDisplay(DisplayType.FLEX);
		content.getStyle().getBackground().setColor(0, 0, 0, 0);
		content.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 1), 1f));
		content.getStyle().setBorderRadius(0F);
		content.getStyle().setShadow(null);
		content.getStyle().setFocusedStrokeColor(0, 0, 0, 0);
	}

	public void winMoney(double money) {
		Label win_money_label = new Label("+" + money);
		win_money_label.autoSizeWidth();
		win_money_label.setVariable(getComponentVariable() + "_win_money_label");
		win_money_label.load();
		win_money_label.getStyle().setFontSize(18f);
		win_money_label.getStyle().setBottom(0f);
		win_money_label.getStyle().setLeft((float) new Random().nextInt(100));

		loadMoneyAnimation(win_money_label).startAnimation();
	}

	public void loseMoney(double money) {
		Label lose_money_label = new Label("-" + money);
		lose_money_label.autoSizeWidth();
		lose_money_label.setVariable(getComponentVariable() + "_lose_money_label");
		lose_money_label.load();
		lose_money_label.getStyle().setFontSize(18f);
		lose_money_label.getStyle().setBottom(0f);
		lose_money_label.getStyle().setRight((float) new Random().nextInt(100));

		loadMoneyAnimation(lose_money_label).startAnimation();
	}

	public Animation loadMoneyAnimation(Label label) {
		Animation animation = new Animation() {

			private double time;

			@Override
			protected void beforeAnimation() {
				top_content.add(label);
			}

			@Override
			protected boolean animate(double delta) {
				time += delta;
				label.getStyle().setBottom((float) ((double) (time * 11D) * 2));

				if (time >= 2.1d)
					return true;
				return false;
			}

			@Override
			protected void afterAnimation() {
				top_content.remove(label);
			}
		};
		return animation;
	}

	public void setMoney(double money) {
		money_label.getTextState().setText(Lang.get("market_money", "%money%", money + ""));
	}

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_market"));

		setMoney(BackdoorGame.getGameClient().getMoney());
		money_label.load();

		offensive_script_label.getTextState().setText(Lang.get("market_offensive_script"));
		offensive_script_label.load();

		defensive_script_label.getTextState().setText(Lang.get("market_defensive_script"));
		defensive_script_label.load();

		description_label.getTextState().setText(Lang.get("market_description"));
		description_label.load();

		script_down_button_label.load();
		script_up_button_label.load();
		script_amount_textfield.load();
		script_total_price_label.load();

		script_name_label.load();

		script_description_text.load();

		buy_button.getTextState().setText(Lang.get("market_buy_button"));
		buy_button.load();

		scrollable_panel_offensive_script.getContainer().clearChildComponents();
		scrollable_panel_defensive_script.getContainer().clearChildComponents();

		int offensive_index = 0;
		int defensive_index = 0;

		for (GameScripts gameScripts : GameScripts.values()) {
			GameScript gameScript = gameScripts.getGameScript();

			Panel component = new Panel();
			component.getStyle().getBackground().setColor(0, 0, 0, 0.2f);
			if (component.getHoveredStyle().getBackground() == null) {
				component.getHoveredStyle().setBackground(new Background());
			}
			if (component.getHoveredStyle().getBackground().getColor() == null) {
				component.getHoveredStyle().getBackground().setColor(new Vector4f());
			}

			component.getHoveredStyle().getBackground().setColor(0, 0, 0, 0.4f);

			component.getStyle().setShadow(null);
			component.getStyle().setBorder(null);
			component.getStyle().setBorderRadius(0f);
			component.getStyle().setDisplay(DisplayType.FLEX);
			component.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
				@Override
				public void process(MouseClickEvent event) {
					if (event.getAction() != MouseClickAction.RELEASE)
						return;

					loadScript(gameScript);
				}
			});

			ImageView icon = new ImageView();
			icon.setImage(BackdoorGame.getDatapack().getImage("icon_script"));
			icon.getStyle().setBorderRadius(0f);
			icon.getStyle().setBorder(null);
			icon.getStyle().setShadow(null);
			icon.getStyle().getBackground().setColor(0, 0, 0, 0);
			icon.getStyle().setTop(10f);
			icon.getStyle().setLeft(10f);
			icon.getStyle().setWidth(30f);
			icon.getStyle().setHeight(30f);
			icon.setFocusable(false);

			Label game_script_name = new Label(gameScript.getName());
			game_script_name.setVariable(getComponentVariable() + "_game_script_name");
			game_script_name.getStyle().setFontSize(16f);
			game_script_name.getStyle().setTop(10f);
			game_script_name.getStyle().setLeft(50f);
			game_script_name.load();
			game_script_name.setFocusable(false);

			Label game_script_price = new Label(gameScript.getPrice() + " BTC");
			game_script_price.setVariable(getComponentVariable() + "_game_script_price");
			game_script_price.getStyle().setFontSize(16f);
			game_script_price.getStyle().setBottom(10f);
			game_script_price.getStyle().setLeft(50f);
			game_script_price.load();
			game_script_price.setFocusable(false);

			float component_height = 50f;
			float component_space = 10f;

			component.getStyle().setHeight(component_height);
			component.getStyle()
					.setTop((gameScripts.getGameScript().getType() == GameScriptType.OFFENSIVE ? offensive_index
							: defensive_index) * (component_height + component_space));
			component.getStyle().setRight(10f);
			component.getStyle().setLeft(10f);

			component.add(game_script_name);
			component.add(game_script_price);
			component.add(icon);
			if (gameScripts.getGameScript().getType() == GameScriptType.OFFENSIVE) {
				scrollable_panel_offensive_script.getContainer().add(component);
				offensive_index++;
			} else if (gameScripts.getGameScript().getType() == GameScriptType.DEFENSIVE) {

				scrollable_panel_defensive_script.getContainer().add(component);
				defensive_index++;
			}
		}
		scrollable_panel_offensive_script.load();
		scrollable_panel_defensive_script.load();

		super.load();
	}
}
