<template>
	<router-view :key="$route.hash" />
</template>

<script setup lang="ts">

	import { GostHalbjahr, GostJahrgang } from "@svws-nrw/svws-core-ts";
	import { ShallowRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";

	const props = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
		halbjahr: ShallowRef<GostHalbjahr>;
	}>();

</script>
