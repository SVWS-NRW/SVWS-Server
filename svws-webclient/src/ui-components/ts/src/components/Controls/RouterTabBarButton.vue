<template>
    <button @click="select()" :class="[isSelected ? 'router-tab-bar-button--active' : '', props.hidden ? 'hidden' : 'router-tab-bar-button']">
        {{ route.meta?.text }}
    </button>
</template>

<script lang="ts" setup>
    import { computed, ComputedRef } from 'vue';
    import { RouteRecordRaw } from "vue-router";

    const emit = defineEmits<{
        (e: 'select', value: RouteRecordRaw) : void
    }>();

    const props = defineProps<{
        route: RouteRecordRaw
        hidden: boolean
        selected: RouteRecordRaw
    }>();

    const isSelected: ComputedRef<boolean> = computed(() => {
        return props.route.name?.toString() === props.selected.name?.toString();
    });

    function select() {
        emit('select', props.route);
    }

</script>


<style lang="postcss">

    .router-tab-bar-button {
        @apply bg-light;
        @apply inline-flex items-center justify-center;
        @apply py-2 px-4;
        @apply rounded-full;
        @apply select-none;
        @apply text-sm font-bold text-dark;
        @apply whitespace-nowrap;
    }

    .router-tab-bar-button:focus {
        @apply outline-none ring ring-primary ring-opacity-75;
    }

    .router-tab-bar-button--active {
        @apply bg-dark;
        @apply text-light;
    }

    .router-tab-bar-button:disabled {
        @apply cursor-not-allowed;
        @apply text-disabled-dark;
    }

</style>
