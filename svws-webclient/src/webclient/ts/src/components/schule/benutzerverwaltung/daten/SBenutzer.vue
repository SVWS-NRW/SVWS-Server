<template>
	<div class="flex flex-row flex-grow">
		<div class="mr-4">
			<s-card-benutzer-daten />
		</div>
		<div class="w-full">
			<s-card-benutzer-kompetenzen />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { App } from "~/apps/BaseApp";

</script>
