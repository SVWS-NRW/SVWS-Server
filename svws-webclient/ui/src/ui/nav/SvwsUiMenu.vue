<template>
	<div class="sidebar--menu focus-region" :class="{'highlighted': focusHelpVisible}">
		<p v-if="focusSwitchingEnabled" v-show="focusHelpVisible" class="region-enumeration">1</p>
		<div class="sidebar--menu--header" v-if="$slots.header">
			<slot name="header" />
		</div>
		<div v-else class="h-5" />
		<div class="sidebar--menu--body">
			<slot />
		</div>
		<div class="sidebar--menu--footer">
			<slot name="footer" />
			<div class="sidebar--menu--footer-credits flex flex-col items-center text-ui-secondary">
				<div class="text-sm mt-2 mb-2 text-center">Powered by<br>SVWS NRW</div>
				<button role="link" @click="show = true"
					class="mb-1 underline hover:text-ui-secondary-hover text-sm">
					Client Info
				</button>
			</div>
		</div>
	</div>
	<svws-ui-modal v-model:show="show" size="small">
		<template #modalTitle>
			SVWS-Client
		</template>
		<template #modalContent>
			<div class="text-left">
				<div class="mb-5">
					Version
					<slot name="version" />
				</div>
				<p class="text-left text-ui-secondary">
					Hinweis: Um eine gute Lesbarkeit zu erzeugen, wird bei SVWS-NRW möglichst auf geschlechtsneutrale
					Begriffe wie Lehrkräfte, Klassenleitung, Erzieher usw. zurückgegriffen. An Stellen, wo das nicht
					möglich ist, wird versucht alle Geschlechter gleichermaßen zu berücksichtigen.
				</p>
			</div>
		</template>
		<template #modalActions>
			<slot name="metaNavigation" />
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const props = withDefaults(defineProps<{
		showEinstellungenDefaultApp? : boolean;
		focusSwitchingEnabled? : boolean;
		focusHelpVisible?: boolean;
	}>(), {
		showEinstellungenDefaultApp: true,
		focusSwitchingEnabled: false,
		focusHelpVisible: false,
	});

	defineSlots();
	const show = ref<boolean>(false);

	// function handleBeforePrint() {
	// 	if (themeRef.value === 'dark') {
	// 		document.documentElement.classList.remove('dark');
	// 		document.documentElement.classList.add('light');
	// 	}
	// }

	// function handleAfterPrint() {
	// 	if (themeRef.value === 'dark') {
	// 		document.documentElement.classList.remove('light');
	// 		document.documentElement.classList.add('dark');
	// 	}
	// }

	// onMounted(() => {
	// 	window.addEventListener("beforeprint", handleBeforePrint);
	// 	window.addEventListener("afterprint", handleAfterPrint);
	// });

	// onUnmounted(() => {
	// 	window.removeEventListener("beforeprint", handleBeforePrint);
	// 	window.removeEventListener("afterprint", handleAfterPrint);
	// });
</script>
