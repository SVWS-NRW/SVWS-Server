<template>
	<tr class="svws-ui-tr">
		<td class="svws-ui-td">
			<div class="flex items-center gap-1">
				<div type="icon" size="small" @click="collapsed = !collapsed" :class="hatSubKompetenzen ? 'cursor-pointer' : 'pointer-events-none'" :tabindex="!hatSubKompetenzen ? -1 : ''">
					<template v-if="hatSubKompetenzen">
						<span class="icon i-ri-arrow-right-s-line" v-if="collapsed" />
						<span class="icon i-ri-arrow-down-s-line" v-else />
					</template>
				</div>
				<svws-ui-checkbox v-model="selected" :indeterminate :disabled="istAdmin">
					{{ kompetenzgruppe.daten.bezeichnung }}
				</svws-ui-checkbox>
			</div>
		</td>
		<td class="svws-ui-td" />
		<template v-if="hatSubKompetenzen">
			<div v-for="kompetenz in benutzerKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id" class="svws-ui-tr" v-show="!collapsed">
				<s-benutzergruppe-kompetenz :kompetenz :show-info :ist-admin :manager :add-kompetenz :remove-kompetenz />
			</div>
		</template>
	</tr>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import type { BenutzerKompetenzGruppe } from "@core/core/types/benutzer/BenutzerKompetenzGruppe";
	import type { BenutzergruppenManager } from "@core/core/utils/benutzer/BenutzergruppenManager";
	import type { List } from "@core/java/util/List";
	import { ref, computed } from "vue";

	const props = defineProps<{
		manager: () => BenutzergruppenManager;
		showInfo: boolean;
		kompetenzgruppe: BenutzerKompetenzGruppe;
		istAdmin: boolean;
		addKompetenz: (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz: (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe: (kompetenzgruppe: BenutzerKompetenzGruppe) => Promise<boolean>,
		removeBenutzerKompetenzGruppe: (kompetenzgruppe: BenutzerKompetenzGruppe) => Promise<boolean>,
		benutzerKompetenzen: (kompetenzgruppe: BenutzerKompetenzGruppe) => List<BenutzerKompetenz>;
	}>();

	const collapsed = ref(true);

	const hatSubKompetenzen = computed<number>(() => props.benutzerKompetenzen(props.kompetenzgruppe).size());

	const selectedHatAlle = computed<boolean>(() => props.manager().hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)));
	const selectedMindestensEine = computed<boolean>(() => props.manager().hatKompetenzenMindestensEine(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)));
	const indeterminate = computed<boolean>(() => !selectedHatAlle.value && selectedMindestensEine.value);

	const selected = computed<boolean>({
		get: () => selectedHatAlle.value,
		set: (value) => {
			if (value) {
				void props.addBenutzerKompetenzGruppe(props.kompetenzgruppe);
			} else {
				void props.removeBenutzerKompetenzGruppe(props.kompetenzgruppe);
			}
		},
	});

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: minmax(4rem, 3fr) 0.15fr;
	}

</style>
