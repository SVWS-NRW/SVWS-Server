<template>
	<div class="w-full min-h-8 flex items-center flex-grow">
		<div v-if="attributeValue !== undefined" @mouseover="pendingIndicatorHovered = true" @mouseleave="pendingIndicatorHovered = false"
			ref="gruppenProzessIconContainer" @click="revert" class="h-auto w-auto me-2" :id="`tooltip-${String(attributeName)}`">
			<SvwsUiTooltip color="primary" :show-arrow="false" :indicator="false">
				<template #content>
					<div class="py-1 px-1 flex flex-col">
						{{ tooltipTextActual }}
						<div class="w-auto bg-white h-0.5 my-2"/>
						{{ tooltipTextAfter }}
					</div>
				</template>
				<div class="h-full min-h-8 w-8 px-1 bg-ui-brand hover:bg-ui-brand-hover rounded content-center center align-middle cursor-pointer">
					<span v-if="pendingIndicatorHovered" class="inline-block i-ri-close-line icon-lg h-full min-h-8 w-full align-middle bg-white"/>
					<span v-else class="inline-block i-ri-information-line icon-lg h-full min-h-8 w-full align-middle bg-white"/>
				</div>
			</SvwsUiTooltip>
		</div>

		<div ref="container" class="grow grid items-center">
			<slot/>
		</div>

		<div class="pt-3 ms-2 me-4 w-auto h-full flex justify-center" :id="`Clearbutton-${String(attributeName)}`">
			<SvwsUiTooltip v-if="nullable" :show-arrow="false" :indicator="false">
				<template #content>
					Aktuellen Wert löschen
				</template>
				<span class="icon-lg i-ri-delete-bin-line" @click="setNull"
					:class="{ 'icon-ui-danger cursor-pointer': showClearButton, 'icon-ui-disabled': !showClearButton }"
				/>
			</SvwsUiTooltip>
			<div v-else class="min-w-6"/>
		</div>
	</div>
</template>

<script setup lang='ts' generic="T">

	import { ref, computed } from 'vue';
	import type { PendingStateManager } from './PendingStateManager';
	import type { JavaMap } from "../../../../core/src/java/util/JavaMap";
	import { HashMap } from "../../../../core/src/java/util/HashMap";

	const props = withDefaults(defineProps<{
		pendingStateManager: () => PendingStateManager<any>,
		attributeName: keyof T,
		nullable?: boolean,
	}>(), {
		nullable: true,
	});

	const container = ref<HTMLElement | null>(null);
	const pendingIndicatorHovered = ref<boolean>(false);

	// Erzeugt ein Record mit den Werten und der Anzahl wie häufig dieser Wert gesetzt wurde
	const actualValuesWithAmount = computed(() => {
		const keyAmounts: JavaMap<any, number> = new HashMap();
		for (const obj of props.pendingStateManager().auswahlManager.getListeDaten().values()) {
			const key = ((obj[props.attributeName] === null) || (obj[props.attributeName] === undefined)) ? 'Keine Daten' : obj[props.attributeName];
			const keyAmount = keyAmounts.get(key);
			keyAmounts.put(key, (keyAmount === null) ? 1 : keyAmount + 1)
		}
		return keyAmounts;
	})

	const attributeValue = computed(() => {
		// Prüfe, ob Attribut in Generic <T> vorhanden sein kann
		let value = undefined;
		if (props.attributeName in props.pendingStateManager().pendingValues)
			value = props.pendingStateManager().pendingValues[props.attributeName];
		return value;
	});

	const pendingAmountOverall = computed<number>(() => {
		let amount = 0;
		for (const keyAmount of actualValuesWithAmount.value.values())
			amount += keyAmount;
		return amount;
	})

	const showClearButton = computed<boolean>(() => {
		for (const key of actualValuesWithAmount.value.keySet())
			if ((key !== 'Keine Daten') && (attributeValue.value !== null))
				return true;
		return false;
	})

	const tooltipTextActual = computed<string>(() => {
		let tooltipActual = 'Aktuell: ';
		for (const entry of actualValuesWithAmount.value.entrySet()) {
			const mapper = props.pendingStateManager().attributeDisplayMappers.get(props.attributeName);
			tooltipActual += ((mapper === undefined) || (entry.getKey() === 'Keine Daten')) ? entry.getKey() : mapper(entry.getKey());
			tooltipActual += ` (${entry.getValue()}), `;
		}
		const tailCut = tooltipActual.slice(0, -2);
		return tailCut.length > 200 ? `${tailCut.slice(0, 200)} ...` : tailCut;
	})

	const tooltipTextAfter = computed(() => {
		let tooltipAfter = 'Nachher: ';
		if ((attributeValue.value !== undefined) && (attributeValue.value !== null)) {
			const mapper = props.pendingStateManager().attributeDisplayMappers.get(props.attributeName);
			tooltipAfter += (mapper !== undefined) ? mapper(attributeValue.value) : attributeValue.value;
		} else
			tooltipAfter += 'Keine Daten';

		tooltipAfter += ` (${pendingAmountOverall.value})`;
		return tooltipAfter;
	})

	// Entfernt den aktuellen pendingState für das Attribut
	function revert() {
		props.pendingStateManager().removePendingState(props.attributeName);
	}

	// Setzt den pendingState für das Attribut auf <code>null</code>
	function setNull() {
		if (!props.nullable || !showClearButton.value)
			return;
		props.pendingStateManager().setPendingState(props.attributeName, null, props.pendingStateManager().auswahlManager.liste.auswahlKeyList());
	}

</script>
