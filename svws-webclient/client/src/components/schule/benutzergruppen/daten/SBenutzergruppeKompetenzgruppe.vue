<template>
	<svws-ui-table-row>
		<svws-ui-table-cell>
			<div class="flex items-center gap-1">
				<svws-ui-button type="icon" size="small" @click="collapsed = !collapsed" :class="{'pointer-events-none': !hatSubKompetenzen}" :tabindex="!hatSubKompetenzen ? -1 : ''">
					<template v-if="hatSubKompetenzen">
						<i-ri-arrow-right-s-line v-if="collapsed" />
						<i-ri-arrow-down-s-line v-else />
					</template>
				</svws-ui-button>
				<svws-ui-checkbox v-model="selected" :disabled="istAdmin">
					{{ kompetenzgruppe.daten.bezeichnung }}
				</svws-ui-checkbox>
			</div>
		</svws-ui-table-cell>
		<svws-ui-table-cell class="font-mono" :class="{'text-black/50 dark:text-white/50': istAdmin}">
			{{ kompetenzgruppe.daten.id }}
		</svws-ui-table-cell>
	</svws-ui-table-row>
	<svws-ui-table-row :depth="1" :collapsed="collapsed" :expanded="!collapsed" v-for="kompetenz in benutzerKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id">
		<s-benutzergruppe-kompetenz :kompetenz="kompetenz" :ist-admin="istAdmin"
			:get-benutzergruppen-manager="getBenutzergruppenManager" :add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" />
	</svws-ui-table-row>
</template>

<script setup lang="ts">

	import type { BenutzergruppenManager, BenutzerKompetenzGruppe, List} from "@core";
	import { BenutzerKompetenz} from "@core";
	import { ref, computed } from "vue";

	const props = defineProps<{
		getBenutzergruppenManager: () => BenutzergruppenManager;
		kompetenzgruppe: BenutzerKompetenzGruppe;
		istAdmin: boolean;
		addKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>,
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>,
		benutzerKompetenzen : (kompetenzgruppe : BenutzerKompetenzGruppe) => List<BenutzerKompetenz>;
	}>();

	const collapsed = ref(true);

	const hatSubKompetenzen = computed<number>(() =>  props.benutzerKompetenzen(props.kompetenzgruppe).size());

	const selectedMindestensEine = computed<boolean>(() => props.getBenutzergruppenManager().hatKompetenzenMindestensEine(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)));

	const selected = computed<'indeterminate' | boolean>({
		get: () => props.getBenutzergruppenManager().hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)) || (selectedMindestensEine.value ? 'indeterminate' : false),
		set: (value) => {
			if (value)
				void props.addBenutzerKompetenzGruppe(props.kompetenzgruppe);
			else
				void props.removeBenutzerKompetenzGruppe(props.kompetenzgruppe);
		}
	});
</script>

<style scoped lang="postcss">
.data-table__tr {
	grid-template-columns: minmax(4rem, 3fr) minmax(4rem, 0.25fr);
}
</style>
