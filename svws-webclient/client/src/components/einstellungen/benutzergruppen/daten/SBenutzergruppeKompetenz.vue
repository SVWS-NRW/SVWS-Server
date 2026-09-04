<template>
	<td class="svws-ui-td ml-9">
		<svws-ui-checkbox v-model="selected" :disabled="aktiviert" class="leading-tight">
			{{ kompetenz.daten.bezeichnung }}
		</svws-ui-checkbox>
	</td>
	<td class="svws-ui-td">
		<template v-if="showInfo">
			<svws-ui-tooltip>
				<span class="icon i-ri-information-line" />
				<template #content>
					{{ kompetenz.daten.tooltip }}
				</template>
			</svws-ui-tooltip>
		</template>
	</td>
</template>

<script setup lang="ts">

	import type { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import type { BenutzergruppenManager } from "@core/core/utils/benutzer/BenutzergruppenManager";
	import { computed } from "vue";

	const props = defineProps<{
		manager: () => BenutzergruppenManager;
		showInfo: boolean;
		kompetenz: BenutzerKompetenz;
		istAdmin: boolean;
		addKompetenz: (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz: (kompetenz: BenutzerKompetenz) => Promise<boolean>;
	}>();

	const aktiviert = computed(() => props.istAdmin);

	const selected = computed<boolean>({
		get: () => props.manager().hatKompetenz(props.kompetenz),
		set: (value) => {
			const alt = props.manager().hatKompetenz(props.kompetenz);
			if (alt === value) {
				return;
			}
			if (value) {
				void props.addKompetenz(props.kompetenz);
			} else {
				void props.removeKompetenz(props.kompetenz);
			}
		},
	});

</script>

