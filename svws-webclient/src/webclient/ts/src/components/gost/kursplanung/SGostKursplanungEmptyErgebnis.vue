<template>
	<div>
		<template v-if="hat_blockung">
			<router-view :key="$route.hash" />
		</template>
		<div v-else>
			Es liegt noch keine Planung für dieses Halbjahr vor. Klicken Sie auf
			"Blockung hinzufügen", um eine neue Kursplanung zu erstellen.
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostHalbjahr, GostJahrgang } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { ListKursblockungen } from "~/apps/gost/ListKursblockungen";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";

	const props = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
		halbjahr: ShallowRef<GostHalbjahr>;
		listBlockungen: ListKursblockungen;
		blockung: DataGostKursblockung;
	}>();

	const hat_blockung: ComputedRef<boolean> = computed(() => props.listBlockungen.liste.length > 0);

</script>
