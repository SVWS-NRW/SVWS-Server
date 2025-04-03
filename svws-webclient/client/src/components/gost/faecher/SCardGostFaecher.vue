<template>
	<svws-ui-table :items="faecherManager().faecher()" :no-data="false" has-background scroll class="h-full">
		<template #header>
			<div class="svws-ui-tr" role="row">
				<div class="svws-ui-td svws-divider col-span-4" role="columnheader">
					<span>Angebotene Fächer</span>
				</div>
				<div class="svws-ui-td svws-align-center svws-divider col-span-2" role="columnheader">
					Leitfächer
				</div>
				<div class="svws-ui-td svws-divider svws-align-center col-span-6" role="columnheader">
					Wählbar
				</div>
				<div class="svws-ui-td svws-align-center col-span-2" role="columnheader">
					Abitur
				</div>
			</div>
			<div class="svws-ui-tr" role="row">
				<div class="svws-ui-td">
					Kürzel
				</div>
				<div class="svws-ui-td">
					Bezeichnung
				</div>
				<div class="svws-ui-td svws-align-center">
					<span class="svws-no-padding">Neu</span>
				</div>
				<div class="svws-ui-td svws-align-center svws-divider">
					<svws-ui-tooltip>
						<span>WS</span>
						<template #content>
							Wochenstunden
						</template>
					</svws-ui-tooltip>
				</div>
				<div class="svws-ui-td svws-align-center svws-no-padding">
					1
				</div>
				<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
					2
				</div>
				<div class="svws-ui-td svws-align-center svws-no-padding">
					EF.1
				</div>
				<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
					EF.2
				</div>
				<div class="svws-ui-td svws-align-center svws-no-padding">
					Q1.1
				</div>
				<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
					Q1.2
				</div>
				<div class="svws-ui-td svws-align-center svws-no-padding">
					Q2.1
				</div>
				<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
					Q2.2
				</div>
				<div class="svws-ui-td svws-align-center svws-no-padding">
					GK
				</div>
				<div class="svws-ui-td svws-align-center svws-no-padding">
					LK
				</div>
			</div>
		</template>
		<template #body>
			<template v-for="fach in faecherManager().faecher()" :key="fach.hashCode()">
				<div class="svws-ui-tr text-ui-static" role="row" :style="{ 'background-color': bgColor(fach) }">
					<s-row-gost-faecher :fach-id="fach.id" :abiturjahr :patch-fach :faecher-manager :hat-update-kompetenz />
				</div>
			</template>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { Fach } from "@core";
	import type { GostFach, GostFaecherManager } from "@core";

	const props = defineProps<{
		faecherManager: () => GostFaecherManager;
		patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<void>;
		abiturjahr: number;
		hatUpdateKompetenz: boolean;
	}>();

	const schuljahr = computed<number>(() => props.faecherManager().getSchuljahr());

	function bgColor(fach : GostFach) : string {
		return Fach.getBySchluesselOrDefault(fach.kuerzel).getHMTLFarbeRGB(schuljahr.value);
	}

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: minmax(5rem, 0.25fr) minmax(12rem, 1fr) minmax(2.5rem, 0.1fr) minmax(3.5rem, 0.25fr) repeat(2, minmax(6rem, 0.25fr)) repeat(8, minmax(3rem, 0.25fr));
	}

</style>
