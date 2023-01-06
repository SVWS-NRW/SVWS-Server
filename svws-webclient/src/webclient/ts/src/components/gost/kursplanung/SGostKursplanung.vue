<template>
	<div
		v-if="visible"
		class="flex"
	>
		<div class="flex flex-row gap-4">
			<s-card-gost-kursansicht />
			<s-card-gost-umwahlansicht class="grow"/>
		</div>
		<div v-if="allow_regeln" class="app-layout--main-sidebar" :class="{ 'app-layout--main-sidebar--collapsed': collapsed }">
			<div class="app-layout--main-sidebar--container">
				<div class="app-layout--main-sidebar--trigger" @click="onToggle">
					<div class="sidebar-trigger--text">
						<svws-ui-button type="icon" class="mr-1 p-[0.1em]" v-if="!collapsed">
							<svws-ui-icon>
								<i-ri-close-line/>
							</svws-ui-icon>
						</svws-ui-button>
						<svws-ui-icon v-if="collapsed" class="mr-2">
							<i-ri-equalizer-line/>
						</svws-ui-icon>
						<span>Regeln zur Blockung</span>
					</div>
					<div class="app-layout--main-sidebar--trigger-count" v-if="collapsed">
						{{ regelzahl }}
					</div>
				</div>
				<div class="app-layout--main-sidebar--content">
					<s-card-gost-regelansicht v-if="!collapsed && active_panel==='regeln'"/>
				</div>
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

	const self = Symbol("kursplanung");

	onMounted(() => main.config.kursblockung_aktiv.add(self));
	onUnmounted(() => main.config.kursblockung_aktiv.delete(self));
	
	const collapsed: Ref<boolean> = ref(true);
	const active_panel: Ref<'regeln'> = ref('regeln')

	const visible: ComputedRef<boolean> =
		computed(() => app.blockungsauswahl.liste.length > 0 && !!app.blockungsauswahl.ausgewaehlt);

	const regelzahl: ComputedRef<number> =
		computed(()=> app.dataKursblockung.datenmanager?.getRegelAnzahl() || 0);
		
	const allow_regeln: ComputedRef<boolean> =
		computed(()=> app.blockungsergebnisauswahl.liste.length === 1);

	function onToggle() {
		collapsed.value = !collapsed.value;
	}
</script>
