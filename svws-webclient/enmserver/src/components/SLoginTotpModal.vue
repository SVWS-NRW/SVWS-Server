<template>
	<svws-ui-modal v-model:show="show" class="hidden">
		<template #modalTitle>
			<div class="flex flex-col">
				Eingabe des zweiten Anmeldefaktors
				<span v-if="expirationSeconds > 0" class="text-sm font-normal font-mono opacity-50">
					(Läuft ab in {{ formattedExpiration }})
				</span>
			</div>
		</template>
		<template #modalContent>
			<div class="flex flex-col gap-2 text-left">
				<template v-if="(auth.totpSetup !== null) && (otpauthUrl !== null)">
					<p class="font-bold mb-2">Es liegt eine Erstanmeldung vor.</p>
					<p class="mb-3">Scannen Sie den QR-Code in Ihrer Authenticator-App oder nutzen Sie das Secret:</p>
					<div class="flex flex-col justify-center items-center gap-3">
						<qr-code :uri="otpauthUrl" />
						<div class="bg-ui-75 p-2 border rounded font-mono select-all">
							{{ auth.totpSetup.secret }}
						</div>
					</div>
					<div class="border-t w-full mt-4 mb-4" />
				</template>
				<p class="font-bold">Geben Sie den Token aus der Authenticator-App ein:</p>
				<svws-ui-text-input v-model="token" placeholder="TOTP Token" :min-len="6" :max-len="6"
					@keydown.enter="checkToken" @methods="handleInputMethodsToken" />
				<div v-if="errorMessage" class="text-ui-danger text-sm font-medium"> {{ errorMessage }} </div>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="cancelLogin"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="checkToken" :disabled="!isTokenValid"> OK </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, nextTick, ref, watch } from "vue";
	import { useAuthState } from "~/states/AuthState";

	const auth = useAuthState();
	const show = defineModel<boolean>("show", { default: false });

	const token = ref<string>("");
	const isTokenValid = computed(() => token.value.length === 6);

	const now = ref(Math.floor(Date.now() / 1000));
	let timer: ReturnType<typeof setInterval> | undefined = undefined;

	const errorMessage = ref<string | null>(null);

	const otpauthUrl = computed<string | null>(() => {
		if (auth.totpSetup === null) {
			return null;
		}
		const issuer = encodeURIComponent(auth.totpSetup.issuer);
		const account = encodeURIComponent(auth.totpSetup.account);
		return `otpauth://totp/${issuer}:${account}?secret=${auth.totpSetup.secret}&issuer=${issuer}`;
	});


	// Greife auf Methoden des Textinputs zurück, um dieses automatisch Fokussieren zu können
	const tokenInput = ref<{ focus: () => void } | undefined>(undefined);
	function handleInputMethodsToken(methods: { focus: () => void } | undefined) {
		tokenInput.value = methods;
	}

	const expirationSeconds = computed(() => {
		return (now.value > 0) ? auth.expirationSeconds : 0;
	});

	const formattedExpiration = computed(() => {
		const s = expirationSeconds.value;
		return `${Math.floor(s / 60)}:${(s % 60).toString().padStart(2, '0')}`;
	});

	// Führe aktionen beim Zeigen oder Verstecken des Modals per Watcher automatisch aus
	watch(show, async (isVisible) => {
		if (isVisible) {
			now.value = Math.floor(Date.now() / 1000);
			timer = globalThis.setInterval(() => {
				now.value = Math.floor(Date.now() / 1000);
			}, 1000);
			await nextTick();
			tokenInput.value?.focus();
		} else {
			if (timer !== undefined) {
				globalThis.clearInterval(timer);
				timer = undefined;
			}
			errorMessage.value = null;
			token.value = "";
		}
	});

	watch(expirationSeconds, async (newValue) => {
		if ((newValue <= 0) && show.value) {
			await cancelLogin();
		}
	});

	/**
	 * Breche den Login-Versuch ab
	 */
	async function cancelLogin(): Promise<void> {
		await auth.logout();
		show.value = false;
	}

	/**
	 * Prüfe das Token uns schließe im Erfolgsfall die Eingabemöglichkeit.
	 */
	async function checkToken(): Promise<void> {
		if (!isTokenValid.value) {
			return;
		}
		errorMessage.value = null;
		const success = await auth.verifyTotp(token.value);
		if (success) {
			show.value = false;
		} else {
			// Bei einem Fehler wieder zur Eingabe zurückkehren
			errorMessage.value = "Der eingegebene Code ist ungültig. Bitte versuchen Sie es erneut.";
			token.value = "";
			tokenInput.value?.focus();
		}
	}

</script>
