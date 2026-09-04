<template>
	<div class="page page-flex-row gap-6 overflow-y-auto">
		<ui-select label="API-Ziel" :manager="apiSelectManager" :searchable="state.searchable" :removable="state.removable"
			:disabled="state.disabled" :statistics="state.statistics" :headless="state.headless"
			:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
	</div>
</template>

<script setup lang="ts">

	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { reactive } from "vue";

	const state = reactive({
		searchable: false,
		disabled: false,
		statistics: false,
		removable: false,
		required: true,
		headless: false,
		minOptions: undefined as number | undefined,
		maxOptions: undefined as number | undefined,
		bgColor: "",
		textColor: "",
		iconColor: "",
		borderColor: "",
		sort: "id" as "id" | "kuerzel" | "text",
	});

	const apiTargets: string[] = ["schulbewerbung.de"];

	const apiSelectManager = new SelectManager({ options: apiTargets });

</script>
