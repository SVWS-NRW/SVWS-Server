<template>
	<div
		v-if="visible && app.blockungsauswahl.ausgewaehlt"
		class="flex"
	>
		<div class="flex flex-row flex-wrap gap-4">
			<s-card-gost-kursansicht />
			<s-card-gost-umwahlansicht class="grow"/>
		</div>
		<div class="flex min-h-full flex-col ml-auto bg-slate-200 rounded-l-lg" :class="{ 'w-16': collapsed }">
			<div class="relative pl-2 ">
				<svws-ui-button class="flex  justify-center w-7 h-7 rounded-full text-headline-4 text-black translate-y-1/2 transform" @click.prevent="onToggle">
					<svws-ui-icon> <i-ri-arrow-right-s-line v-if="collapsed" /> <i-ri-arrow-left-s-line v-else /> </svws-ui-icon>
				</svws-ui-button>
			</div>
			<div class="flex flex-col py-4">
				<s-card-gost-regelansicht />
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

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.gostKursplanung.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

	const self = Symbol("kursplanung");

	onMounted(() => main.config.kursblockung_aktiv.add(self));
	onUnmounted(() => main.config.kursblockung_aktiv.delete(self));

	function onToggle() {
		collapsed.value = !collapsed.value;
	}
</script>
