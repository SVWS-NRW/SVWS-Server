<template>
	<div
		v-if="visible"
		class="flex"
	>
		<div class="flex flex-row flex-wrap gap-4">
			<s-card-gost-kursansicht />
			<s-card-gost-umwahlansicht class="grow"/>
		</div>
		<div class="flex min-h-full flex-col ml-auto bg-slate-200 rounded-l-lg" :class="{ 'w-24': collapsed }">
			<div class="relative pl-2 flex flex-col gap-2">
				<svws-ui-button class="flex justify-center w-20 h-7 rounded-full text-headline-4 text-black translate-y-1/2 transform" @click="onToggleRegeln">
					Regeln <svws-ui-icon> <i-ri-arrow-left-s-line v-if="collapsed" /> <i-ri-arrow-right-s-line v-else /> </svws-ui-icon> </svws-ui-button>
				<svws-ui-button class="flex justify-center w-20 h-7 rounded-full text-headline-4 text-black translate-y-1/2 transform" @click="onToggleLehrer">
					Lehrer <svws-ui-icon> <i-ri-arrow-left-s-line v-if="collapsed" /> <i-ri-arrow-right-s-line v-else /> </svws-ui-icon> </svws-ui-button>
			</div>
			<div class="flex flex-col py-4">
				<s-card-gost-regelansicht v-if="!collapsed && active_panel==='regeln'"/>
				<s-card-gost-kurslehrerpanel v-if="!collapsed && active_panel==='lehrer'"/>
			</div>
		</div>
	</div>
	<div v-else>
		Es liegt noch keine Planung für dieses Halbjahr vor. Klicken Sie auf
		"Blockung hinzufügen", um eine neue Kursplanung zu erstellen.
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, onMounted, onUnmounted, Ref, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;
	
	const collapsed: Ref<boolean> = ref(true);
	const active_panel: Ref<'lehrer'|'regeln'> = ref('regeln')

	const visible: ComputedRef<boolean> =
		computed(() =>
		//return this.$app.gostKursplanung.visible; //TODO: richtige Bedingung einpflegen
		app.blockungsauswahl.liste.length > 0 && !!app.blockungsauswahl.ausgewaehlt);

	const self = Symbol("kursplanung");

	onMounted(() => main.config.kursblockung_aktiv.add(self));
	onUnmounted(() => main.config.kursblockung_aktiv.delete(self));

	function onToggle() {
		collapsed.value = !collapsed.value;
	}

	function onToggleRegeln() {
		if (active_panel.value === 'regeln')
			onToggle();
		active_panel.value = 'regeln';
	}
	function onToggleLehrer() {
		if (active_panel.value === 'lehrer')
			onToggle();
		active_panel.value = 'lehrer';
	}
</script>
