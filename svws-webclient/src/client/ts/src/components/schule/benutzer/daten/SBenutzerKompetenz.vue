<template>
	<div role="cell" class="data-table__td" :class="{'vorhanden' : selected && !aktiviert, 'nichtvorhanden' : !selected && !aktiviert, 'deaktiviert':aktiviert }" :title="aktiviert ? 'Bereits aktiviert durch Benutzergruppe' : ''">
		<svws-ui-checkbox v-model="selected" :disabled="aktiviert">
			{{ kompetenz.daten.bezeichnung }}
		</svws-ui-checkbox>
	</div>
	<div role="cell" class="data-table__td font-mono" :class="{'vorhanden' : selected && !aktiviert, 'nichtvorhanden' : !selected && !aktiviert, 'deaktiviert':aktiviert }" :title="aktiviert ? 'Bereits aktiviert durch Benutzergruppe' : ''">
		{{ kompetenz.daten.id }}
	</div>
	<div role="cell" class="data-table__td" :class="{'vorhanden' : selected && !aktiviert, 'nichtvorhanden' : !selected && !aktiviert, 'deaktiviert':aktiviert }" :title="aktiviert ? 'Bereits aktiviert durch Benutzergruppe' : ''">
		<!-- TODO Die Methode in Manager auslagern. -->
		<div class="data-table__td-content">
			<template v-if="getBenutzerManager().istAdmin()">
				<i-ri-shield-star-line/>
			</template>
			<template v-else>
				{{ props.getGruppen4Kompetenz(kompetenz) }}
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { BenutzerKompetenz, BenutzerManager } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, WritableComputedRef } from "vue";

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

<style scoped lang="postcss">
	.vorhanden {
		@apply bg-primary/5 text-primary;
	}

	.nichtvorhanden {
		@apply bg-white;
	}

	.deaktiviert {
		@apply text-black/50;
	}

	.data-table__td-content {
		@apply inline-block;
	}
</style>
