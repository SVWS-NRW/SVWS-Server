<template>
	<svws-ui-content-card title="Kompetenzen" class="col-span-full">
		<template #actions>
			<svws-ui-checkbox class="mb-4 " v-model="inputIstAdmin" :disabled="getBenutzerManager().istInAdminGruppe()">
				<span class="inline-flex items-center">
					Admin-Rechte
				</span>
			</svws-ui-checkbox>
		</template>
		<svws-ui-data-table :items="kompetenzgruppen" :disable-footer="true">
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr">
					<div role="columnheader"
						class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<span class="data-table__th-title">
								<span class="inline-flex items-center gap-1">
									<span>Kompetenz</span>
								</span>
							</span>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<span class="data-table__th-title font-mono">
								<span class="inline-flex items-center gap-1">
									<span>ID</span>
								</span>
							</span>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<span class="data-table__th-title">
								<span class="inline-flex items-center gap-1">
									<span>Benutzergruppe</span>
								</span>
							</span>
						</div>
					</div>
				</div>
			</template>
			<template #body>
				<template v-for="(kompetenzgruppe, index) in kompetenzgruppen"
					:key="index">
					<s-benutzer-kompetenzgruppe :kompetenzgruppe="kompetenzgruppe" :get-benutzer-manager="getBenutzerManager"
						:add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" :get-gruppen4-kompetenz="getGruppen4Kompetenz"
						:add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe"
						:remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe" />
				</template>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { BenutzerKompetenz, BenutzerManager } from "@svws-nrw/svws-core";
	import { BenutzerKompetenzGruppe } from "@svws-nrw/svws-core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		getBenutzerManager: () => BenutzerManager;
		setIstAdmin : (istAdmin: boolean) => Promise<void>;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		getGruppen4Kompetenz : ( kompetenz : BenutzerKompetenz ) => string;
	}>();

	const kompetenzgruppen: ComputedRef<BenutzerKompetenzGruppe[]> = computed(() => BenutzerKompetenzGruppe.values().filter(gr => gr.daten.id >= 0));

	const inputIstAdmin: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.getBenutzerManager().istAdmin(),
		set: (value) => {
			if ((value === undefined) || (value === props.getBenutzerManager().istAdmin()))
				return;
			void props.setIstAdmin(value);
		}
	});
</script>

<style scoped lang="postcss">
	.data-table__tr {
		grid-template-columns: minmax(4rem, 2fr) minmax(4rem, 0.5fr) minmax(4rem, 1fr);
	}

	.tooltip {
	position: relative;
	display: inline-block;
	border-bottom: 1px dotted black;
	}

	.tooltip .tooltiptext {
	visibility: hidden;
	width: 120px;
	background-color: rgb(175, 215, 231);
	color: #fff;
	text-align: center;
	border-radius: 6px;
	padding: 5px 0;

	/* Position the tooltip */
	position: absolute;
	z-index: 1;
	}
</style>
