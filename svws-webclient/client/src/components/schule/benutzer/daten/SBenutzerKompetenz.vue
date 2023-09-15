<template>
	<div class="svws-ui-td ml-9" role="cell">
		<svws-ui-checkbox v-model="selected" :disabled="aktiviert" class="leading-tight">
			{{ kompetenz.daten.bezeichnung }}
		</svws-ui-checkbox>
	</div>
	<div class="svws-ui-td" :title="getGruppen4Kompetenz(kompetenz)" role="cell">
		<!-- TODO Die Methode in Manager auslagern. -->
		<template v-if="!getBenutzerManager().istAdmin()">
			<span class="line-clamp-1 break-all text-black/50 dark:text-white/50">{{ getGruppen4Kompetenz(kompetenz) }}</span>
		</template>
	</div>
	<div class="svws-ui-td svws-align-right" role="cell">
		{{ kompetenz.daten.id }}
	</div>
</template>

<script setup lang="ts">
	import type { BenutzerKompetenz, BenutzerManager } from "@core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		getBenutzerManager: () => BenutzerManager;
		kompetenz: BenutzerKompetenz;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		getGruppen4Kompetenz : ( kompetenz : BenutzerKompetenz ) => string;
	}>();

	// True wenn Benutzer Admin ist oder die Kompetenz von einer Gruppe geerbt wird.
	const aktiviert : ComputedRef<boolean | undefined> = computed(() => {
		return props.getBenutzerManager().istAdmin() || (props.getBenutzerManager().getGruppen(props.kompetenz).size() !== 0)
	});

	const selected: WritableComputedRef<boolean> = computed({
		get: () => props.getBenutzerManager().hatKompetenz(props.kompetenz),
		set: (value) => {
			if (value)
				void props.addKompetenz(props.kompetenz);
			else
				void props.removeKompetenz(props.kompetenz);
		}
	});
</script>
