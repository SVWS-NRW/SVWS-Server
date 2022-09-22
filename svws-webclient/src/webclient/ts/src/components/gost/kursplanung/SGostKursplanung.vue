<template>
	<div
		v-if="visible && app.blockungsauswahl.ausgewaehlt"
		class="app-container"
	>
		<s-card-gost-kursplanung />
		<s-card-gost-blockung />
		<s-card-gost-kursblockung-parameter />
	</div>
	<div v-else>
		Es liegt noch keine Planung für dieses Halbjahr vor. Klicken Sie auf
		"Blockung hinzufügen", um eine neue Kursplanung zu erstellen.
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, onMounted, onUnmounted } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	const main: Main = injectMainApp();
	const app = main.apps.gost;
	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.gostKursplanung.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
	const self = Symbol("kursplanung");
	onMounted(() => main.config.kursblockung_aktiv.add(self));
	onUnmounted(() => main.config.kursblockung_aktiv.delete(self));
</script>
