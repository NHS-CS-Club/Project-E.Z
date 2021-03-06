@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		System.out.println("Made by NHS Coding Club!");
	}
}
