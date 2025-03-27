<template>
	<div class="svws-ui-td ml-9" role="cell">
		<svws-ui-checkbox v-model="selected" :disabled="aktiviert" class="leading-tight">
			{{ kompetenz.daten.bezeichnung }}
		</svws-ui-checkbox>
	</div>
	<div class="svws-ui-td" role="cell">
		<template v-if="showInfo">
			<svws-ui-tooltip>
				<span class="icon i-ri-information-line" />
				<template #content>
					{{ kompetenz.daten.tooltip }}
				</template>
			</svws-ui-tooltip>
		</template>
	</div>
	<div class="svws-ui-td" :title="getBenutzerManager().getBenutzerGruppenString(kompetenz)" role="cell">
		<!-- TODO Die Methode in Manager auslagern. -->
		<template v-if="!getBenutzerManager().istAdmin()">
			<span class="line-clamp-1 break-all text-black/50 dark:text-white/50">{{ getBenutzerManager().getBenutzerGruppenString(kompetenz) }}</span>
		</template>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { BenutzerKompetenz, BenutzerManager } from "@core";

	const props = defineProps<{
		getBenutzerManager: () => BenutzerManager;
		showInfo: boolean;
		kompetenz: BenutzerKompetenz;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
	}>();

	// True wenn Benutzer Admin ist oder die Kompetenz von einer Gruppe geerbt wird.
	const aktiviert = computed<boolean | undefined>(() => {
		return props.getBenutzerManager().istAdmin() || (props.getBenutzerManager().getGruppen(props.kompetenz).size() !== 0)
	});

	const selected = computed<boolean>({
		get: () => props.getBenutzerManager().hatKompetenz(props.kompetenz),
		set: (value) => {
			if (value)
				void props.addKompetenz(props.kompetenz);
			else
				void props.removeKompetenz(props.kompetenz);
		},
	});

</script>
